package com.gateway.dto;

import com.gateway.dto.ServiceRole;

import java.util.List;
import java.util.UUID;

public class ServiceUser {
    private UUID id;
    private String email;
    private String password;
    private String address;
    private String firstName;
    private String lastName;
    private List<ServiceRole> roles;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ServiceRole> getRoles() {
        return roles;
    }

    public void setRoles(List<ServiceRole> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
