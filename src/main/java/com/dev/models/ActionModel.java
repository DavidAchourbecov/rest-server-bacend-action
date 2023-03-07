package com.dev.models;

import com.dev.objects.Action;

public class ActionModel {
    private int id;
    private double userBid;


    public ActionModel(Action action) {
        this.id = action.getId();
        this.userBid = action.getUserSuggestAmount();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getUserBid() {
        return userBid;
    }

    public void setUserBid(double userBid) {
        this.userBid = userBid;
    }
}
