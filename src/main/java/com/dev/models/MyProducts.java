package com.dev.models;

import com.dev.objects.Action;
import com.dev.objects.Product;

public class MyProducts {
    private int id;
    private String name;
    private double bidMax;
    private boolean openToAction;

    public MyProducts(Product product, double bidMax)  {
        this.id = product.getId();
        this.name = product.getProductName();
        this.bidMax = bidMax;
        this.openToAction = product.getOpenToAction();
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

