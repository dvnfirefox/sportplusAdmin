package com.bot.adminfront.service;

import com.bot.adminfront.model.Partie;
import com.bot.adminfront.model.Tournoi;
import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TournoisService {

    public boolean creez(LocalDate debut, LocalDate fin, int maximum, String categorie, String federation) {
        ObjectNode json = Json.createNode();
        json.put("datedebut", debut.toString());
        json.put("datefin", fin.toString());
        json.put("maximum", maximum);
        json.put("categorie", categorie);
        json.put("federation", federation);

        JsonNode result = HttpService.post("tournois/creez", json.toString());
        return result != null && result.has("status") && result.get("status").asBoolean();
    }

    public List<Tournoi> recherche(String keyword, String champ) {
        List<Tournoi> tournoisList = new ArrayList<>();
        JsonNode arrayNode = null;

        try {
            switch (champ.toLowerCase()) {
                case "debut", "avant", "apres", "fin" -> {
                    String backendType = switch (champ.toLowerCase()) {
                        case "debut" -> "debut.before";
                        case "fin" -> "fin.after";
                        case "avant" -> "debut.before";
                        case "apres" -> "debut.after";
                        default -> "";
                    };
                    arrayNode = HttpService.get(
                            "tournois/recherche?date=" + keyword + "&type=" + backendType
                    );
                }
                case "entre" -> {
                    String[] dates = keyword.split(",");
                    if (dates.length == 2) {
                        arrayNode = HttpService.get(
                                "tournois/recherche/between?debut=" + dates[0] + "&fin=" + dates[1] + "&type=debut"
                        );
                    }
                }
                default -> arrayNode = HttpService.get(
                        "tournois/recherche/champ?keyword=" + keyword + "&champ=" + champ
                );
            }

            if (arrayNode != null && arrayNode.isArray()) {
                for (JsonNode node : arrayNode) {
                    String id = node.get("id").asText();
                    String debut = node.get("dateDebut").asText();
                    String fin = node.get("dateFin").asText();
                    String maximum = node.get("equipeMaximum").asText();
                    String federation = node.get("federation").asText();
                    String categorie = node.get("categorie").asText();

                    Tournoi tournoi = new Tournoi(id, debut, fin, maximum, federation, categorie);

                    // Parse parties array ← ADD THIS SECTION
                    if (node.has("parties") && node.get("parties").isArray()) {
                        List<Partie> parties = new ArrayList<>();
                        for (JsonNode partieNode : node.get("parties")) {
                            // We just need to know if parties exist, so we can create minimal Partie objects
                            Partie partie = new Partie();
                            partie.setId(partieNode.get("id").asLong());
                            parties.add(partie);
                        }
                        tournoi.setParties(parties);
                    }

                    tournoisList.add(tournoi);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tournoisList;
    }

    /**
     * Supprime un tournoi par son ID
     */
    public void supprimer(String id) {
        ObjectNode json = Json.createNode();
        json.put("id", id);
        HttpService.post("tournois/supprimer", json.toString());
    }

    /**
     * Modifie un tournoi (envoi l'objet complet au backend)
     */
    public boolean modifier(Tournoi t) {
        try {
            ObjectNode json = Json.createNode();
            json.put("id", Long.parseLong(t.getId()));
            json.put("datedebut", t.getDebut());
            json.put("datefin", t.getFin());
            json.put("categorie", t.getCategorie());
            json.put("federation", t.getFederation());
            json.put("maximum", Integer.parseInt(t.getMaximum()));

            JsonNode result = HttpService.post("tournois/modifier", json.toString());
            System.out.println(result.toString());
            return result.asBoolean();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void calendrier(String id) {
        ObjectNode json = Json.createNode();
        json.put("id", id);
        String jsonString = json.toString();
        System.out.println("Sending JSON: " + jsonString);  // Debug line
        HttpService.post("partie/calendrier", jsonString);
    }
}
