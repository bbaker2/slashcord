package com.bbaker.slashcord.structure.results;

import static com.bbaker.slashcord.util.CommonsUtil.isNotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javacord.api.exception.BadRequestException;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.util.CommonsUtil;

public class FailedResult implements UpsertResult {

    private Throwable t;
    private Command def;
    private Operation operation;

    public FailedResult(Throwable t, Command def, Operation operation) {
        this.t = t;
        this.def = def;
        this.operation = operation;
    }


    @Override
    public SlashCommand getCommand() {
        return null;
    }

    @Override
    public Command getDefinition() {
        return this.def;
    }

    @Override
    public Operation getOperartion() {
        return this.operation;
    }

    @Override
    public boolean successful() {
        return false;
    }

    @Override
    public List<Error> getMessage() {
        return parseThrowable(t);
    }

    @Override
    public String toString() {
        String msg = getMessage().stream()
                .map(e -> String.format("%-40s%s", e.getMessage(), e.getLoc()))
                .collect(Collectors.joining("\n  "));
        if(isNotBlank(msg)) {
            msg = "\n  " + msg;
        }
        return String.format("%-16s %8s was unsuccessful%s",
                "["+def.getName()+"]",
                operation,
                msg);
    }

    public static Function<Throwable, UpsertResult> insert(Command def){
        return t -> new FailedResult(t, def, Operation.INSERT);
    }

    public static Function<Throwable, UpsertResult> update(Command def){
        return t -> new FailedResult(t, def, Operation.UPDATE);
    }

    public List<Error> parseThrowable(Throwable t){
        if(t.getCause() != null && t.getCause() instanceof BadRequestException) {
            return parseException((BadRequestException)t.getCause());
        }

        return Arrays.asList(new GenericError(t));
    }


    private List<Error> parseException(BadRequestException e) {
        Optional<String> response = e.getResponse().flatMap(RestRequestResponseInformation::getBody);
        Optional<String> request =  e.getRequest().flatMap(RestRequestInformation::getBody);


        if(request.isPresent() && response.isPresent()) {
            List<Error> errors = traverseJson(request.get(), response.get());
            if(errors != null) {
                return errors;
            }
        }
        return Arrays.asList(new GenericError(e));
    }

    private List<Error> traverseJson(String jsonRequest, String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONObject request = new JSONObject(jsonRequest);
            JSONObject errors = response.getJSONObject("errors");
            String name = CommonsUtil.isBlank(def.getName()) ? "COMMAND" : def.getName();
            return traverse(request, "/" + name, errors, "errors");
        } catch (Throwable t) {
            return null;
        }

    }

    private List<Error> traverse(Object request, String path, JSONObject response, String parentRespKey){
        String[] keys = JSONObject.getNames(response);
        List<Error> errors = new ArrayList<Error>();
        for(String key : keys) {
            // if we entered an _errors property, that means we're at a leaf
            // and we can stop diving down
            if("_errors".equals(key)) {
                JSONArray child = response.getJSONArray(key);
                String messages = String.join(" AND ", traverseErrors(child));
                errors.add(new DiscordError(path, messages, t));
            } else {
                JSONObject respChild = response.getJSONObject(key);
                Object requChild;
                int index = getIndex(key);
                String newPath = path;

                if(index == -1) {
                    // If the key is not an index,
                    // traverse normally
                    requChild = ((JSONObject)request).get(key);
                    newPath += "." + key;
                } else {
                    // Otherwise, assume we are trying to traverse an array
                    requChild = ((JSONArray)request).get(index);
                    newPath += "[" + index + "]";
                }
                errors.addAll(traverse(requChild, newPath, respChild, key));
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

   private int getIndex(String key) {
       try {
           return Integer.valueOf(key);
       } catch (Throwable t) {
           return -1;
       }
   }


}
