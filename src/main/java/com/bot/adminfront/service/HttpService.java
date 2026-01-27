package com.bot.adminfront.service;
import com.bot.adminfront.tool.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {
    private static final ObjectMapper mapper = new ObjectMapper();
    // une methode qui permet de faire des demande au serveur pour permettre les differente transaction avec
    // la base de donner et de la transformation des donne
    public static ObjectNode post(String endpoint, String body) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the response body string into ObjectNode
            return (ObjectNode) mapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HTTP request failed", e);
        }
    }
    /**
     * Generic GET request with query parameters
     * @param endpoint The API endpoint (e.g., "utilisateur/recherche?keyword=ma")
     * @return ObjectNode containing the JSON response
     */
    public static JsonNode get(String endpoint) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + endpoint))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readTree(response.body()); // returns JsonNode (object or array)
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HTTP GET request failed", e);
        }
    }
}
