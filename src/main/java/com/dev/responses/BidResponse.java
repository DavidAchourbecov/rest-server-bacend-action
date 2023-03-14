package com.dev.responses;

public class BidResponse extends BasicResponse{
    private int id;
    private double userBid;

    public BidResponse(boolean success, Integer errorCode, int id, double userBid) {
        super(success, errorCode);
        this.id = id;
        this.userBid = userBid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getUserBid() {
        return userBid;
    }

    public void setUserBid(double userBid) {
        this.userBid = userBid;
    }
}
