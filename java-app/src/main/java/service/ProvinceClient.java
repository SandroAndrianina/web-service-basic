package main.java.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.model.Province;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProvinceClient {

    private static final String URL = "http://127.0.0.1:8081/api/province";

    public static List<Province> getAll() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("PHP API returned " + response.statusCode());
        }

        Type listType = new TypeToken<List<Province>>() {}.getType();
        return new Gson().fromJson(response.body(), listType);
    }

}