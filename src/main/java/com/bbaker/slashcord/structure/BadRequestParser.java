package com.bbaker.slashcord.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.javacord.api.exception.BadRequestException;
import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;
import org.json.JSONArray;
import org.json.JSONObject;

public class BadRequestParser {

    public static List<String> parseThrowable(Throwable t){
        if(t.getCause() != null && t.getCause() instanceof BadRequestException) {
            return parseException((BadRequestException)t.getCause());
        }

        return Arrays.asList(t.getMessage());
    }


    private static List<String> parseException(BadRequestException e) {
        Optional<String> response = e.getResponse().flatMap(RestRequestResponseInformation::getBody);
        Optional<String> request =  e.getRequest().flatMap(RestRequestInformation::getBody);


        if(request.isPresent() && response.isPresent()) {
            List<String> errors = traverseJson(request.get(), response.get());
            if(errors != null) {
                return errors;
            }
        }
        return Arrays.asList(e.getMessage());
    }

    private static List<String> traverseJson(String jsonRequest, String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONObject request = new JSONObject(jsonRequest);
            JSONObject errors = response.getJSONObject("errors");
            return traverse(request, errors);
        } catch (Throwable t) {
            return null;
        }

    }

    private static List<String> traverse(JSONObject request, JSONObject response){
        String[] keys = JSONObject.getNames(response);
        List<String> errors = new ArrayList<String>();
        for(String key : keys) {
            if("_errors".equals(key)) {
                JSONArray child = response.getJSONArray(key);
                errors.addAll(traverseErrors(child));
            } else {
                JSONObject child = response.getJSONObject(key);
                errors.addAll(traverse(request, child));
            }
        }

        return errors;
    }

   private static List<String> traverseErrors(JSONArray array){
       List<String> errors = new ArrayList<String>();
       for(int i = 0; i < array.length(); i++) {
           JSONObject each = array.getJSONObject(i);
           errors.add(each.getString("message"));
       }
       return errors;
   }

}
