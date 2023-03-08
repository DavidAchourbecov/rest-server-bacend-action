package com.dev.models;

import com.dev.objects.Action;

public class MyBidsModel {
    private int id;
    private String name;
    private double bid;
    private boolean openToAction;
    private int isWinner;

    public MyBidsModel(Action action) {
        this.id = action.getProduct().getId();
        this.name = action.getProduct().getProductName();
        this.bid = action.getUserSuggestAmount();
        this.openToAction = action.getProduct().getOpenToAction();
        this.isWinner = action.isWinner();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public boolean isOpenToAction() {
        return openToAction;
    }

    public void setOpenToAction(boolean openToAction) {
        this.openToAction = openToAction;
    }

    public int getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(int isWinner) {
        this.isWinner = isWinner;
    }
}
