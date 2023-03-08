package com.dev.models;

import com.dev.objects.Action;
import com.dev.objects.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductModel {
    private int id;
    private String productName;
    private String content;
    private String imageLink;
    private double minimumPrice;
    private Boolean openToAction;
    private String publishDate;
    private String username;
    private int numOpenBids;
    private List<ActionModel> myBids;


    public ProductModel(Product product,int  numOpenBids,List<Action> action){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.content = product.getContent();
        this.imageLink = product.getImageLink();
        this.minimumPrice = product.getMinimumPrice();
        this.openToAction = product.getOpenToAction();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.publishDate = simpleDateFormat.format(product.getPublishDate());
        this.username = product.getUser().getUsername();
        this.numOpenBids = numOpenBids;
        this.myBids =new ArrayList<>();
        for (Action a : action){
            this.myBids.add(new ActionModel(a));
        }



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

    public double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Boolean getOpenToAction() {
        return openToAction;
    }

    public void setOpenToAction(Boolean openToAction) {
        this.openToAction = openToAction;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumOpenBids() {
        return numOpenBids;
    }

    public void setNumOpenBids(int numOpenBids) {
        this.numOpenBids = numOpenBids;
    }

    public List<ActionModel> getMyBids() {
        return myBids;
    }

    public void setMyBids(List<ActionModel> myBids) {
        this.myBids = myBids;
    }


}
