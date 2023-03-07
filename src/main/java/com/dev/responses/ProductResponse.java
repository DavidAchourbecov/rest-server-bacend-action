package com.dev.responses;

import com.dev.models.ProductModel;
import com.dev.objects.Product;

public class ProductResponse extends BasicResponse{

    private ProductModel product;

    public ProductResponse(boolean success, Integer errorCode,ProductModel  product) {
        super(success, errorCode);
        this.product = product;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }
}
