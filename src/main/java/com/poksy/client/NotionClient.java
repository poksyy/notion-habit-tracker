package com.poksy.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poksy.config.NotionConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class NotionClient {

    private final HttpClient httpClient;

    public NotionClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public Optional<String> createPage(String jsonBody) {
        try {
            HttpRequest request = buildRequest("/pages")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                return Optional.of(json.get("id").getAsString());
            }

            logError("API Error", response);
            return Optional.empty();

        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean archivePage(String pageId) {
        try {
            String json = """
                    { "archived": true }
                    """;

            HttpRequest request = buildRequest("/pages/" + pageId)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logError("Archive Error", response);
            }

            return response.statusCode() == 200;

        } catch (Exception e) {
            System.err.println("Archive failed: " + e.getMessage());
            return false;
        }
    }

    public Optional<String> queryDatabase(String databaseId, String filterJson) {
        try {
            HttpRequest request = buildRequest("/databases/" + databaseId + "/query")
                    .POST(HttpRequest.BodyPublishers.ofString(filterJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(response.body());
            }

            logError("Query Error", response);
            return Optional.empty();

        } catch (Exception e) {
            System.err.println("Query failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    private HttpRequest.Builder buildRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(NotionConfig.NOTION_API_URL + endpoint))
                .header("Authorization", "Bearer " + NotionConfig.NOTION_TOKEN)
                .header("Notion-Version", NotionConfig.NOTION_VERSION)
                .header("Content-Type", "application/json");
    }

    private void logError(String prefix, HttpResponse<String> response) {
        System.err.println(prefix + " [" + response.statusCode() + "]: " + response.body());
    }
}