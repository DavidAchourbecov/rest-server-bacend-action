package com.dev.models;

import com.dev.objects.Action;

public class MyProducts {
    private int id;
    private String name;
    private double bidMax;
    private boolean openToAction;

     public MyProducts(Action action) {
        this.id = action.getProduct().getId();
        this.name = action.getProduct().getProductName();
        this.bidMax = action.getUserSuggestAmount();
        this.openToAction = action.getProduct().getOpenToAction();
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

    public double getBidMax() {
        return bidMax;
    }

    public void setBidMax(double bidMax) {
        this.bidMax = bidMax;
    }

    public boolean isOpenToAction() {
        return openToAction;
    }

    public void setOpenToAction(boolean openToAction) {
        this.openToAction = openToAction;
    }
}

