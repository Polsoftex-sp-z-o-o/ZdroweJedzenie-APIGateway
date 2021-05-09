package com.gateway.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class JsonRequestHelper {
    public static String Get(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.connect();

        return new BufferedReader(
                new InputStreamReader((InputStream) connection.getContent(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
    }
}
