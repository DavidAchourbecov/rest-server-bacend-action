package com.dev.objects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Action")
public class Action {

    public Action() {
    }

    public Action( User userSuggest, Product product, User publisher, Date biddingDate, boolean isWinner) {
        this.userSuggest = userSuggest;
        this.product = product;
        this.publisher = publisher;
        this.biddingDate = biddingDate;
        this.isWinner = isWinner;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "suggester_id")
    private User userSuggest;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;



    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;



    @Column(name = "bidding_date")
    private Date biddingDate;

    @Column(name = "winner")
    private boolean isWinner;

}
