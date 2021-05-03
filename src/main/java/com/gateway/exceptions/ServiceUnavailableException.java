package com.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URL;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceUnavailableException extends Exception {
    public ServiceUnavailableException(String servicePath){
        super("Gateway service: " + servicePath + " not available");
    }
}
