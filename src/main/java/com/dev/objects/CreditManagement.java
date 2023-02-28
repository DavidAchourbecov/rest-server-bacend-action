package com.dev.objects;

import javax.persistence.*;

@Entity
@Table(name = "credit_management")

public class CreditManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private int creditAmount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public CreditManagement(int creditAmount, User user) {
        this.creditAmount = creditAmount;
        this.user = user;
    }

    public CreditManagement() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

