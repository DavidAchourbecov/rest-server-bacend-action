package com.dev.models;

import com.dev.objects.Action;
import com.dev.objects.Product;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainTableModel {
    private int id;
    private String name;
    private String linkImage;
    private String date;
    private  int generalBids;
    private  int myBids;
    private int publishUserId;
    private int statement;
    private String bidToken;

     public MainTableModel(Product product, List<Action> actions,int userId, int statement, String bidToken) {
        this.id = product.getId();
        this.name = product.getProductName();
        this.linkImage = product.getImageLink();
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.date = simpleDateFormat.format(product.getPublishDate());
        this.generalBids = actions.size();
        this.statement = statement;
        this.publishUserId = product.getUser().getId();
        this.bidToken = bidToken;
        this.myBids = 0;
        if (userId == 0){
            return;
        }
        for (Action a : actions){
            if (a.getUserSuggest().getId() == userId){
                this.myBids++;
            }
        }



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

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGeneralBids() {
        return generalBids;
    }

    public void setGeneralBids(int generalBids) {
        this.generalBids = generalBids;
    }

    public int getMyBids() {
        return myBids;
    }

    public void setMyBids(int myBids) {
        this.myBids = myBids;
    }

    public int getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(int publishUserId) {
        this.publishUserId = publishUserId;
    }

    public int getStatement() {
        return statement;
    }

    public void setStatement(int statement) {
        this.statement = statement;
    }



    public String getBidToken() {
        return bidToken;
    }

    public void setBidToken(String bidToken) {
        this.bidToken = bidToken;
    }
}
