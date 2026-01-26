package com.bot.adminfront.service;

import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurService {


    //verification des donne forunie par le formulaire de creation dun utilisateur
    public ObjectNode creez(String nom, String password, boolean admin) {
        ObjectNode json = Json.createNode();
        ObjectNode response = Json.createNode();
        if(admin){
            json.put("admin", "admin");
        }else{
            json.put("admin", "none");
        }
        json.put("nom", nom);
        json.put("password", password);

        ObjectNode result = HttpService.post("utilisateur/creez", json.toString());
        if (result != null) {
            response.put("message", result.get("message"));
            response.put("status", result.get("status"));
        }else {
            response.put("message", "erreur de connection");
        }
        return response;
    }
    public List<String> recherche(String keyword) {
        JsonNode arrayNode = HttpService.get("utilisateur/recherche?keyword=" + keyword);
        List<String> noms = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                noms.add(node.asText());
            }
        }
        return noms;
    }
    public void supprimer(String nom){
        ObjectNode json = Json.createNode();
        json.put("nom", nom);
        //faire un call api avec sur ladresse utilisateur/supprimer avec le nom demander
        HttpService.post("utilisateur/supprimer", json.toString());
    }
}

