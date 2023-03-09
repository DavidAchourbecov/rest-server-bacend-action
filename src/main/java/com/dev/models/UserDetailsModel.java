package com.dev.models;

import com.dev.objects.CreditManagement;
import com.dev.objects.Product;

import java.util.List;

public class UserDetailsModel {
    private int id;
    private double amount;
    private int openActions;


    public UserDetailsModel(CreditManagement creditManagement, List<Product> products) {
        this.id = creditManagement.getUser().getId();
        this.amount = creditManagement.getCreditAmount();
        this.openActions = products.size();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getOpenActions() {
        return openActions;
    }

    public void setOpenActions(int openActions) {
        this.openActions = openActions;
    }
}
