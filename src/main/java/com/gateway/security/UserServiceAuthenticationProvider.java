package com.gateway.security;

import com.gateway.dto.ServiceUser;
import com.gateway.exceptions.InvalidCredentialsException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Component
public class UserServiceAuthenticationProvider implements AuthenticationProvider{

    private static final String GET_USER_BY_USERNAME_PATH = "/users/find/";
    private final String userServiceBaseUrl;

    @Value("${add-trailing-slash-when-forwarding}")
    private boolean addTrailingSlash;

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
            String path = userServiceBaseUrl + GET_USER_BY_USERNAME_PATH + username;

            if(!path.endsWith("/") && addTrailingSlash)
                path += "/";

            URL getUserByUserNameUrl = new URL(path);
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
