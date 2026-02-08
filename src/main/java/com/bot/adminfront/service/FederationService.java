package com.bot.adminfront.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public class FederationService {

    public List<String> list() {
        JsonNode arrayNode = HttpService.get("federation/list");
        List<String> federations = new ArrayList<>();

        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                federations.add(node.asText());
            }
        }

        return federations;
    }
}