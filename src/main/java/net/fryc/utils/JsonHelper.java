package net.fryc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.springframework.lang.Nullable;

import java.io.IOException;

public class JsonHelper {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static String removeFieldFromJsonString(String jsonString, String fieldName, @Nullable Logger logger) {
        try{
            JsonNode root = MAPPER.readTree(jsonString);

            if (root.isObject()) {
                ((ObjectNode) root).remove(fieldName);
            }

            return MAPPER.writeValueAsString(root);

        } catch(IOException e){
            if(logger != null){
                String message = "An error occurred while removing field named '" + fieldName + "' from the following JSON string: " + jsonString;
                logger.error(message, e);
            }

            return jsonString;
        }
    }

}
