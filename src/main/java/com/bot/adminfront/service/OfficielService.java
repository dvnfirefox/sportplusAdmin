package com.bot.adminfront.service;

import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class OfficielService {

    public boolean creez(String nom, String phone, String courriel,
                         String adresse, String federation, String role) {

        ObjectNode json = Json.createNode();
        json.put("nom", nom);
        json.put("phone", phone);
        json.put("courriel", courriel);
        json.put("adresse", adresse);
        json.put("federation", federation);
        json.put("role", role);

        JsonNode result = HttpService.post("officiel/creez", json.toString());

        if (result == null) {
            return false; // request failed or no response
        }

        if (!result.has("status")) {
            return false; // malformed response
        }

        return result.get("status").asBoolean();
    }
    public List<String> recherche(String keyword, String mode) {
        JsonNode arrayNode = HttpService.get("officiel/recherche?keyword=" + keyword + "&mode=" + mode);
        List<String> noms = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                noms.add(node.asText());
            }
        }
        return noms;
    }
    public void supprimer(String id){
        ObjectNode json = Json.createNode();
        json.put("id", id);
        //faire un call api avec sur ladresse officiel/supprimer avec le nom demander
        HttpService.post("officiel/supprimer", json.toString());
    }

}
