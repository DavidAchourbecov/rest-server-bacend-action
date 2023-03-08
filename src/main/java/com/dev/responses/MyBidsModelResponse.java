package com.dev.responses;

import com.dev.models.MyBidsModel;

import java.util.List;

public class MyBidsModelResponse extends  BasicResponse{
    private List <MyBidsModel> myBids;

    public MyBidsModelResponse(boolean success, Integer errorCode, List <MyBidsModel> myBids) {
        super(success, errorCode);
        this.myBids = myBids;
    }

    public List <MyBidsModel> getMyBids() {
        return myBids;
    }

    public void setMyBids(List <MyBidsModel> myBids) {
        this.myBids = myBids;
    }
}
