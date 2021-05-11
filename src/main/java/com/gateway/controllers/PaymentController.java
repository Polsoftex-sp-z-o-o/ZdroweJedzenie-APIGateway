package com.gateway.controllers;

import com.gateway.dto.Card;
import com.gateway.exceptions.CartNotFoundException;
import com.gateway.exceptions.InsufficientAuthorityException;
import com.gateway.security.GatewayPrincipal;
import com.gateway.services.PaymentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
@CrossOrigin
@RestController
public class PaymentController  {
    private final PaymentService _paymentService;

    public PaymentController(PaymentService paymentService) {
        _paymentService = paymentService;
    }

    @Secured("ROLE_USER")
    @PostMapping("/payment")
    public void makePayment(
            @RequestBody Card paymentInfo
            , @RequestParam("userid") UUID userId
            , @AuthenticationPrincipal GatewayPrincipal principal) throws InsufficientAuthorityException, IOException, CartNotFoundException {

        if(!principal.getUserId().equals(userId))
            throw new InsufficientAuthorityException();

        _paymentService.makePayment(paymentInfo, userId);
    }
}
