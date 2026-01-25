package com.bot.adminfront.service;

import com.bot.adminfront.tool.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UtilisateurService {

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
}
