package org.example.qlthuvien.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class OpenAIService  {

    @Value("${openai.api_key}")
    private String API_KEY;

    private final String BASE_URL = "https://api.openai.com/v1";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public String sendMessage(String message) throws Exception {
        String assistantId = "asst_D0CDwEqcUhCWIPrU4kzPqAop";
        String threadId = createThread();
        addUserMessage(threadId, message);
        String runId = runAssistant(threadId, assistantId);
        waitForRunCompletion(threadId, runId);
        return getAssistantMessages(threadId);
    }

    private  String createThread() throws Exception {
        System.out.println("API Key: " + API_KEY);

        System.out.println(API_KEY);
        System.out.println("API Key: " + API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/threads"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .header("OpenAI-Beta", "assistants=v2")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println("THREAD ID");
        return parseJson(response.body(), "id");
    }

    private void addUserMessage(String threadId, String content) throws Exception {
        String json = String.format("""
            {
              "role": "user",
              "content": "%s"
            }
            """, content);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/threads/" + threadId + "/messages"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("OpenAI-Beta", "assistants=v2")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String runAssistant(String threadId, String assistantId) throws Exception {
        String json = String.format("""
            {
              "assistant_id": "%s"
            }
            """, assistantId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/threads/" + threadId + "/runs"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("OpenAI-Beta", "assistants=v2")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return parseJson(response.body(), "id");
    }

    private void waitForRunCompletion(String threadId, String runId) throws Exception {

//        System.out.println("Waiting for run completion...");
//        System.out.println(threadId);
//        System.out.println(runId);
//        System.out.println("!@#");
        String status = "";
        while (!"completed".equals(status)) {
            Thread.sleep(250); // đợi 2s
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/threads/" + threadId + "/runs/" + runId))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("OpenAI-Beta", "assistants=v2")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = parseJson(response.body(), "status");
            System.out.println("Run status: " + status);
        }
    }

    private String getAssistantMessages(String threadId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/threads/" + threadId + "/messages"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("OpenAI-Beta", "assistants=v2")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode root = objectMapper.readTree(response.body());

        // Parse first message -> content -> first item -> text -> value
        JsonNode messages = root.get("data");
        if (messages != null && messages.isArray() && messages.size() > 0) {
            JsonNode contentArray = messages.get(0).get("content");
            if (contentArray != null && contentArray.isArray() && contentArray.size() > 0) {
                JsonNode textNode = contentArray.get(0).get("text");
                if (textNode != null && textNode.has("value")) {
                    String assistantReply = textNode.get("value").asText();
                    System.out.println("\nAssistant response:\n" + assistantReply);
                    return assistantReply;
                }
            }
        }
        return "";
    }

    private static String parseJson(String json, String key) {
        int idx = json.indexOf("\"" + key + "\"");
        if (idx == -1) return null;
        int start = json.indexOf(":", idx) + 1;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return json.substring(start, end).replaceAll("[\"\\s]", "");
    }
}
