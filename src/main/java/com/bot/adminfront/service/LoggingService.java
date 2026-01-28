package com.bot.adminfront.service;

import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LoggingService {

    //fait un json pour permmetre la transmission plus simple via un api
    public Boolean logging(String nom, String password) {
        ObjectNode json = Json.createNode();
        json.put("nom", nom);
        json.put("password", password);
        JsonNode result = HttpService.post("session/connection", json.toString());
        System.out.println(result);
        if ((result != null) && result.get("connection").asBoolean()) {
            return true;
        }else {
            return false;
        }
    }
}




