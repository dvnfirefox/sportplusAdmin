package com.bot.adminfront.service;

import com.bot.adminfront.model.Equipe;
import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class EquipesService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Equipe> recherche(String keyword, String mode) {
        // URL encode the keyword to handle spaces and special characters
        String encodedKeyword = HttpService.encodeValue(keyword != null ? keyword : "");
        String encodedMode = HttpService.encodeValue(mode != null ? mode : "");

        String endpoint = "equipe/recherche?keyword=" + encodedKeyword + "&mode=" + encodedMode;
        JsonNode arrayNode = HttpService.get(endpoint);

        List<Equipe> equipes = new ArrayList<>();

        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                try {
                    Equipe equipe = mapper.treeToValue(node, Equipe.class);
                    equipes.add(equipe);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return equipes;
    }

    public boolean supprimer(Long id) {
        try {
            ObjectNode json = Json.createNode();
            json.put("id", id.toString());
            JsonNode response = HttpService.post("equipe/supprimer", json.toString());
            return response != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}