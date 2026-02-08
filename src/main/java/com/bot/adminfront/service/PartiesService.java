package com.bot.adminfront.service;

import com.bot.adminfront.model.Partie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PartiesService {

    private static final ObjectMapper mapper = new ObjectMapper();

    // ================= API METHODS =================

    /**
     * Fetches all parties based on search criteria
     * Converts backend nested JSON to frontend flat Partie model
     */
    public List<Partie> recherche(String keyword, String mode) {
        if (keyword == null || keyword.isEmpty() || mode == null || mode.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Build endpoint based on mode
            String endpoint;
            if (mode.equalsIgnoreCase("avant") || mode.equalsIgnoreCase("apres") ||
                    mode.equalsIgnoreCase("now") || mode.equalsIgnoreCase("date")) {  // ← ADD "date" HERE
                // Date-based search
                endpoint = "partie/recherchedate?type=" + mode + "&date=" + keyword;
            } else {
                // Text-based search (tournois, local, visiteur)
                String type = mapModeToType(mode);
                endpoint = "partie/recherche?type=" + type + "&text=" + keyword;
            }

            JsonNode response = HttpService.get(endpoint);

            return convertJsonToParties(response);

        } catch (Exception e) {
            System.err.println("Error fetching parties: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Modifies an existing partie - send updated points to backend
     */
    public boolean modifier(Partie partie) {
        try {
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("id", partie.getId());
            requestBody.put("pointLocal", Integer.parseInt(partie.getPointLocal()));
            requestBody.put("pointVisiteur", Integer.parseInt(partie.getPointVisiteur()));

            JsonNode response = HttpService.post("partie/pointage", requestBody.toString());

            // Assuming backend returns boolean or success message
            return true;

        } catch (Exception e) {
            System.err.println("Error modifying partie: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches parties for a specific tournament
     */
    public List<Partie> getPartiesByTournoi(String tournoiId) {
        try {
            String endpoint = "partie/recherche?type=tournois&text=" + tournoiId;
            JsonNode response = HttpService.get(endpoint);

            return convertJsonToParties(response);

        } catch (Exception e) {
            System.err.println("Error fetching parties by tournoi: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ================= HELPER METHODS =================

    /**
     * Maps frontend mode to backend type parameter
     */
    private String mapModeToType(String mode) {
        return switch (mode.toLowerCase()) {
            case "tournoi" -> "tournois";
            case "equipelocal" -> "local";
            case "equipevisiteur" -> "visiteur";
            default -> mode;
        };
    }

    /**
     * Convert backend JSON array to List<Partie>
     * Backend JSON has nested objects, we flatten to frontend model
     */
    private List<Partie> convertJsonToParties(JsonNode jsonNode) {
        List<Partie> parties = new ArrayList<>();

        if (jsonNode == null || !jsonNode.isArray()) {
            return parties;
        }

        for (JsonNode node : jsonNode) {
            try {
                Partie p = new Partie();

                p.setId(node.get("id").asLong());
                p.setDate(node.get("date").asText());
                p.setPointLocal(String.valueOf(node.get("pointLocal").asInt()));
                p.setPointVisiteur(String.valueOf(node.get("pointVisiteur").asInt()));

                // Extract nested tournois ID
                if (node.has("tournois") && !node.get("tournois").isNull()) {
                    p.setTournoi(String.valueOf(node.get("tournois").get("id").asLong()));
                }

                // Extract nested equipe names
                if (node.has("equipeLocal") && !node.get("equipeLocal").isNull()) {
                    p.setEquipeLocal(node.get("equipeLocal").get("nom").asText());
                }

                if (node.has("equipeVisiteur") && !node.get("equipeVisiteur").isNull()) {
                    p.setEquipeVisiteur(node.get("equipeVisiteur").get("nom").asText());
                }

                parties.add(p);

            } catch (Exception e) {
                System.err.println("Error parsing partie: " + e.getMessage());
                // Skip this partie and continue
            }
        }

        return parties;
    }
}