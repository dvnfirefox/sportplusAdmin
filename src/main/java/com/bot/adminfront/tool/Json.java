package com.bot.adminfront.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Json {

    private static ObjectMapper mapper = new ObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    public static JsonNode toJson(String json) throws JsonProcessingException {
        return mapper.readTree(json);

    }

    public static <A> A fromJson(JsonNode json, Class<A> clazz) throws JsonProcessingException {
        return mapper.treeToValue(json, clazz );
    }

    public static ObjectNode createNode(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        return responseNode;
    }
}
