package com.example.anti_spam_telegram_bot.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class PythonServerClient {

    public PythonServerClient() {}
    public String check_for_spam(String inputText) {
        String url = "http://localhost:8080/predict"; // URL сервера Python

        String output = "";

        // Создаем JSON с запросом
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("text", inputText);

        // Создаем HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();

        // Создаем HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                .build();

        // Отправляем запрос и получаем ответ
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Выводим результат
            if (response.statusCode() == 200) {

                // Пример парсинга JSON
                JSONObject jsonResponse = new JSONObject(response.body());
                output = jsonResponse.get("result").toString();
            } else {
                output= String.valueOf(response.statusCode());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}