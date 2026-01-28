package com.bot.adminfront.service;


import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.time.LocalDate;





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

}
