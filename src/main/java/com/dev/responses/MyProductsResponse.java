package com.dev.responses;

import com.dev.models.MyProducts;

import java.util.List;

public class MyProductsResponse extends  BasicResponse {
    private List <MyProducts> myProducts;

    public MyProductsResponse(boolean success, Integer errorCode, List <MyProducts> myProducts) {
        super(success, errorCode);
        this.myProducts = myProducts;
    }

    public List <MyProducts> getMyProducts() {
        return myProducts;
    }

    public void setMyProducts(List <MyProducts> myProducts) {
        this.myProducts = myProducts;
    }

}
