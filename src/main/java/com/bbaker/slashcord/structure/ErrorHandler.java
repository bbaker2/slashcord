package com.bbaker.slashcord.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.javacord.api.exception.BadRequestException;
import org.javacord.api.interaction.SlashCommand;
import org.json.JSONArray;
import org.json.JSONObject;

public class ErrorHandler implements Function<Throwable, UpsertResult> {

    private SlashCommand preExisting;
    private Operation operation;

    public ErrorHandler(Operation operartion, SlashCommand actual) {
        this.operation = operartion;
        this.preExisting = actual;
    }

    @Override
    public UpsertResult apply(Throwable e) {
        List<String> errorMsg;
        if(e.getCause() instanceof BadRequestException) {
            errorMsg = parseException((BadRequestException)e.getCause());
        } else {
            errorMsg = Arrays.asList(e.getMessage());
        }
        return new UpsertResult() {

            @Override
            public boolean successful() {
                return false;
            }

            @Override
            public Operation getOperartion() {
                return operation;
            }

            @Override
            public List<String> getMessage() {
                return errorMsg;
            }

            @Override
            public SlashCommand getCommand() {
                return preExisting;
            }
        };
    }

    private List<String> parseException(BadRequestException e) {
        return e.getResponse()
                .flatMap(r -> r.getBody())
                .flatMap(this::traverseJson)
                .orElse(Arrays.asList(e.getMessage()));

    }

    private Optional<List<String>> traverseJson(String json) {
        try {
            JSONObject response = new JSONObject(json);
            JSONObject errors = response.getJSONObject("errors");
            return Optional.of(traverse(errors));
        } catch (Throwable t) {
            return Optional.empty();
        }

    }

    private List<String> traverse(JSONObject json){
        String[] keys = JSONObject.getNames(json);
        List<String> errors = new ArrayList<String>();
        for(String key : keys) {
            if("_errors".equals(key)) {
                JSONArray child = json.getJSONArray(key);
                errors.addAll(traverseErrors(child));
            } else {
                JSONObject child = json.getJSONObject(key);
                errors.addAll(traverse(child));
            }
        }

        return errors;
    }

   private List<String> traverseErrors(JSONArray array){
       List<String> errors = new ArrayList<String>();
       for(int i = 0; i < array.length(); i++) {
           JSONObject each = array.getJSONObject(i);
           errors.add(each.getString("message"));
       }
       return errors;
   }

}
