package com.dev.objects;

import javax.persistence.*;

@Entity
@Table (name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    public Product() {
    }

    public Product( String productName, String content, String imageLink, String minimumPrice, String sailed, Boolean openToAction, Object publishDate, User user) {
        this.productName = productName;
        this.content = content;
        this.imageLink = imageLink;
        this.minimumPrice = minimumPrice;
        this.sailed = sailed;
        this.openToAction = openToAction;
        this.publishDate = publishDate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(String minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public String getSailed() {
        return sailed;
    }

    public void setSailed(String sailed) {
        this.sailed = sailed;
    }

    public Boolean getOpenToAction() {
        return openToAction;
    }

    public void setOpenToAction(Boolean openToAction) {
        this.openToAction = openToAction;
    }

    public Object getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Object publishDate) {
        this.publishDate = publishDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "product_name")
    private String productName;

    @Column
    private String content;

    @Column(name = "image")
    private String imageLink;

    @Column(name = "minimum_price")
    private String minimumPrice;

    @Column (name = "sailed")
    private String  sailed;

    @Column(name = "open_to_action")
    private Boolean openToAction;

    @Column(name = "publish_date")
    private Object publishDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
