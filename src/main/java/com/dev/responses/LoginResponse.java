package com.dev.responses;

public class LoginResponse extends BasicResponse{
    private String token;
    private  boolean isAdmin;


    public LoginResponse(boolean success, Integer errorCode, String token, boolean isAdmin) {
        super(success, errorCode);
        this.token = token;
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
