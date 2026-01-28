package com.bot.adminfront.service;


import com.bot.adminfront.model.Tournoi;
import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

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

        if (result == null) {
            return false;
        }

        if (!result.has("status")) {
            return false;
        }

        return result.get("status").asBoolean();
    }

    public List<Tournoi> recherche(String keyword, String champ) {
        List<Tournoi> tournoisList = new ArrayList<>();
        JsonNode arrayNode = null;

        try {
            switch (champ.toLowerCase()) {
                case "debut":
                case "avant":
                case "apres":
                case "fin":
                    // Map ChoiceBox value to backend type
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
                    break;

                case "entre":
                    // keyword format: "startDate,endDate"
                    String[] dates = keyword.split(",");
                    if (dates.length == 2) {
                        String start = dates[0];
                        String end = dates[1];
                        arrayNode = HttpService.get(
                                "tournois/recherche/between?debut=" + start + "&fin=" + end + "&type=debut"
                        );
                    }
                    break;

                default:
                    // text search
                    arrayNode = HttpService.get(
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

                    tournoisList.add(new Tournoi(id, debut, fin, maximum, federation, categorie));
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
}
