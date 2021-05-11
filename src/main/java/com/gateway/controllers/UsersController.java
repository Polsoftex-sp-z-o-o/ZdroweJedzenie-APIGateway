package com.gateway.controllers;

import com.gateway.exceptions.InsufficientAuthorityException;
import com.gateway.security.GatewayPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.UUID;
@CrossOrigin
@RestController
public class UsersController extends GatewayController{
    protected UsersController(
            @Value("${service.address.users}") String productsServiceUrl)
            throws URISyntaxException {
        super(new URI(productsServiceUrl));
    }

    @PostMapping(value="/users", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void HandleAccountCreation(HttpServletRequest req, HttpServletResponse resp) throws ServiceUnavailableException {
        forwardRequest(req, resp);
    }

    @RequestMapping(
            value = {"/users/{userId}"},
            method = {RequestMethod.PUT, RequestMethod.DELETE})
    @Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
    public void HandleAccountEdit(
            @AuthenticationPrincipal GatewayPrincipal principal
            , HttpServletRequest req
            , HttpServletResponse resp
            , @PathVariable UUID userId) throws InsufficientAuthorityException, ServiceUnavailableException {

        validateUserAction(principal, userId);
        forwardRequest(req, resp);
    }
}
