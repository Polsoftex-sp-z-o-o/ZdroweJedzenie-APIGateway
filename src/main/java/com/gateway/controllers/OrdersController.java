package com.gateway.controllers;

import com.gateway.exceptions.InsufficientAuthorityException;
import com.gateway.security.GatewayPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class OrdersController extends GatewayController{
    protected OrdersController(@Value("${service.address.orders}") String ordersServiceUrl) throws URISyntaxException {
        super(new URI(ordersServiceUrl));
    }

    @RequestMapping(
            value = {"/cart", "/orders"},
            method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE})
    @Secured("ROLE_USER")
    public void HandleUserCartOperations(
            HttpServletRequest req
            , HttpServletResponse resp
            , @RequestParam("userid") UUID userId
            , @AuthenticationPrincipal GatewayPrincipal principal) throws InsufficientAuthorityException, ServiceUnavailableException {

        validateUserAction(principal, userId);
        forwardRequest(req, resp);
    }
}
