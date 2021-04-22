package com.gateway.security;

public class ServiceUser {
    private int id;
    private String username;
    private String password;
    private String address;
    private boolean elevation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean getElevation() {
        return elevation;
    }

    public void setElevation(boolean elevation) {
        this.elevation = elevation;
    }
}
