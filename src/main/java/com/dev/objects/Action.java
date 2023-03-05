package com.dev.objects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Action")
public class Action {

    public Action() {
    }

    public Action( User userSuggest,double userSuggestAmount, Product product, User publisher,boolean lastOffer ,Date biddingDate, boolean isWinner) {
        this.userSuggest = userSuggest;
        this.product = product;
        this.publisher = publisher;
        this.biddingDate = biddingDate;
        this.isWinner = isWinner;
        this.userSuggestAmount = userSuggestAmount;
        this.lastOffer = lastOffer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUserSuggest() {
        return userSuggest;
    }

    public void setUserSuggest(User userSuggest) {
        this.userSuggest = userSuggest;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public Date getBiddingDate() {
        return biddingDate;
    }

    public void setBiddingDate(Date biddingDate) {
        this.biddingDate = biddingDate;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public double getUserSuggestAmount() {
        return userSuggestAmount;
    }

    public void setUserSuggestAmount(double userSuggestAmount) {
        this.userSuggestAmount = userSuggestAmount;
    }

    public boolean isLastOffer() {
        return lastOffer;
    }

    public void setLastOffer(boolean lastOffer) {
        this.lastOffer = lastOffer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;


    @ManyToOne
    @JoinColumn(name = "suggester_id")
    private User userSuggest;

    @Column(name = "amount_suggester")
    private double userSuggestAmount;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;



    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;

    @Column(name = "last_offer")
    private boolean lastOffer;



    @Column(name = "bidding_date")
    private Date biddingDate;

    @Column(name = "winner")
    private boolean isWinner;

}
