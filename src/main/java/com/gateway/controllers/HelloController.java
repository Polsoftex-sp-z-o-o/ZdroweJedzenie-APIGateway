package com.gateway.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
@CrossOrigin
@RestController()
public class HelloController {

    @RequestMapping("/greet/anyone")
    public String greetAnyone() {
        return "Hello anyone!";
    }

    @Secured("ROLE_USER")
    @RequestMapping("/greet/user")
    public String greetUser() {
        return "Hello authorized user!";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/greet/admin")
    public String greetAdmin() {
        return "Hello admin!";
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping("/greet/name")
    public String greetName(Principal principal)
    {
        return "hello" + principal.getName();
    }


}