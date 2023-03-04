package com.dev.controllers;

import com.dev.objects.Message;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.ProductResponse;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.dev.utils.Errors.*;

@RestController
public class ProductController {
    @Autowired
    private Persist persist;

    @RequestMapping(value = "add-product", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse addProduct(String token,String productName, String content, String imageLink, String minimumPrice, String sailed, Boolean openToAction) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            Product product = new Product(productName, content, imageLink, minimumPrice, sailed, openToAction, user);
            persist.saveProduct(product);
            basicResponse = new BasicResponse(true, null);
        } else {
            basicResponse = new BasicResponse(false, ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }

    @RequestMapping(value = "get-product", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse getProduct(String token, int productId) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            Product product =null;
            product = persist.getProductById(productId);
            if (product != null) {
                basicResponse = new ProductResponse(true, null, product);
            } else {
                basicResponse = new BasicResponse(false, ERROR_NO_SUCH_PRODUCT);
            }

        } else {
            basicResponse = new BasicResponse(false, ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }
}
