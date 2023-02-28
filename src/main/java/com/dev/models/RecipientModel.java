package com.dev.models;

import com.dev.objects.User;

public class RecipientModel {
    private int id;
    private String username;

    public RecipientModel (User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

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
}
