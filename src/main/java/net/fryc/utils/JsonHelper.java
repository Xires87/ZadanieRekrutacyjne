package net.fryc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonHelper {

    public static String removeFieldFromJsonString(String jsonString, String fieldName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonString);

        if (root.isObject()) {
            ((ObjectNode) root).remove(fieldName);
        }

        return mapper.writeValueAsString(root);
    }
}
