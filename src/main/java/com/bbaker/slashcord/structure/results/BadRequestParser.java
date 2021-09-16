package com.bbaker.slashcord.structure.results;

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

    public static List<Error> parseThrowable(Throwable t){
        if(t.getCause() != null && t.getCause() instanceof BadRequestException) {
            return parseException((BadRequestException)t.getCause());
        }

        return Arrays.asList(new GenericError(t));
    }


    private static List<Error> parseException(BadRequestException e) {
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

    private static List<Error> traverseJson(String jsonRequest, String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONObject request = new JSONObject(jsonRequest);
            JSONObject errors = response.getJSONObject("errors");
            return traverse(request, "[ROOT]", errors, "errors");
        } catch (Throwable t) {
            return null;
        }

    }

    private static List<Error> traverse(Object request, String path, JSONObject response, String parentRespKey){
        String[] keys = JSONObject.getNames(response);
        List<Error> errors = new ArrayList<Error>();
        for(String key : keys) {
            // if we entered an _errors property, that means we're at a leaf
            // and we can stop diving down
            if("_errors".equals(key)) {
                JSONArray child = response.getJSONArray(key);
                String messages = String.join(" AND ", traverseErrors(child));
                errors.add(new DiscordError(path, messages, null));
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

   private static List<String> traverseErrors(JSONArray array){
       List<String> errors = new ArrayList<String>();
       for(int i = 0; i < array.length(); i++) {
           JSONObject each = array.getJSONObject(i);
           errors.add(each.getString("message"));
       }
       return errors;
   }

   private static int getIndex(String key) {
       try {
           return Integer.valueOf(key);
       } catch (Throwable t) {
           return -1;
       }
   }

}
