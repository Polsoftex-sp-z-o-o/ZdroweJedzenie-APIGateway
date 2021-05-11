package com.gateway.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
@CrossOrigin
@SuppressWarnings("MVCPathVariableInspection")
@RestController
public class ProductsController extends GatewayController {

    protected ProductsController(
            @Value("${service.address.products}") String productsServiceUrl)
                throws URISyntaxException {
        super(new URI(productsServiceUrl));
    }

    @GetMapping(value = {"/products", "/products/{id}"})
    public void HandlePublicRequests(HttpServletRequest req, HttpServletResponse resp) throws ServiceUnavailableException {
        forwardRequest(req, resp);
    }

    @RequestMapping(
            value = {"/products", "/products/{id}"},
            method = {RequestMethod.POST,RequestMethod.PUT, RequestMethod.DELETE})
    @Secured("ROLE_ADMIN")
    public void HandleAdminRequests(HttpServletRequest req, HttpServletResponse resp) throws ServiceUnavailableException {
        forwardRequest(req, resp);
    }
}
