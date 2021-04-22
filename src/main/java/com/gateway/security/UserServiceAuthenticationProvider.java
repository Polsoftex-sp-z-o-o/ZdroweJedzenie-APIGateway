package com.gateway.security;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Component
public class UserServiceAuthenticationProvider implements AuthenticationProvider{

    private static final String GET_USER_BY_USERNAME_PATH = "/users/find/";
    private final String userServiceBaseUrl;

    public UserServiceAuthenticationProvider(
            @Value("${users.service.host}") String userServiceBaseUrl) {
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

    @Override
    public AuthenticatedUser Authenticate(String username, String password) throws InvalidCredentialsException, IOException {
        ServiceUser serviceUser =  LoadUserByUsername(username);

        if(!serviceUser.getPassword().equals(password))
            throw new InvalidCredentialsException();

        return AuthenticatedUser.FromServiceUser(serviceUser);
    }

    private ServiceUser LoadUserByUsername(String username) throws InvalidCredentialsException, IOException {
        try {
            URL getUserByUserNameUrl = new URL(userServiceBaseUrl + GET_USER_BY_USERNAME_PATH + username);
            HttpURLConnection connection = (HttpURLConnection) getUserByUserNameUrl.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();

            String jsonUser = new BufferedReader(
                    new InputStreamReader((InputStream) connection.getContent(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            return new Gson().fromJson(jsonUser, ServiceUser.class);
        }
        catch(FileNotFoundException | MalformedURLException e){
            throw new InvalidCredentialsException();
        }
    }
}
