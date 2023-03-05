package com.dev.controllers;

import com.dev.objects.Action;
import com.dev.objects.CreditManagement;
import com.dev.objects.Message;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.ProductResponse;
import com.dev.utils.Constants;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

import java.util.List;

import static com.dev.utils.Errors.*;

@RestController
public class ProductController {
    @Autowired
    private Persist persist;

    @RequestMapping(value = "add-product", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse addProduct(String token, String productName, String content, String imageLink, double minimumPrice,  Boolean openToAction) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            CreditManagement creditManagement = persist.getCreditManagement(user.getId());
            if (creditManagement.getCreditAmount() < minimumPrice) {
                basicResponse = new BasicResponse(false, ERROR_NOT_ENOUGH_CREDIT);
                return basicResponse;
            } else {
                Product product = new Product(productName, content, imageLink, minimumPrice, openToAction, user);
                persist.saveProduct(product);
                creditManagement.setCreditAmount(creditManagement.getCreditAmount() - 2);
                persist.updateAmount(creditManagement);
                User admin = persist.getUserByUsername("admin");
                CreditManagement creditManagementAdmin = persist.getCreditManagement(admin.getId());
                creditManagementAdmin.setCreditAmount(creditManagementAdmin.getCreditAmount() + 2);
                persist.updateAmount(creditManagementAdmin);
                basicResponse = new BasicResponse(true, null);

            }


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
            Product product = null;
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

    @RequestMapping(value = "closed-action", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse closedAction(String token, int productId) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            Product product = null;
            product = persist.getProductById(productId);
            if (product != null) {
                List<Action> actions = persist.getActionsByProductId(productId);
                if (actions.size() < 3) {
                    basicResponse = new BasicResponse(false, ERROR_NOT_ENOUGH_ACTION);
                    return basicResponse;
                } else {
                  Action action=   persist.updateWinnerActionsByProductIdMaxAmountLastDate(productId);


                  persist.updateCreditManagementToLoserActionByProductId(productId,true,action.getUserSuggest());
                  CreditManagement creditManagement=persist.getCreditManagement(user.getId());
                  double amount=this.calculateFeeAmount(action.getUserSuggestAmount());
                  creditManagement.setCreditAmount(amount);
                  persist.updateAmount(creditManagement);
                    User admin = persist.getUserByUsername("admin");
                    CreditManagement creditManagementAdmin = persist.getCreditManagement(admin.getId());
                    creditManagementAdmin.setCreditAmount(action.getUserSuggestAmount() - amount);
                    persist.updateAmount(creditManagementAdmin);

                    product.setOpenToAction(false);
                    persist.updateProduct(product);
                    basicResponse = new BasicResponse(true, null);
                }

            } else {
                basicResponse = new BasicResponse(false, ERROR_NO_SUCH_TOKEN);
            }
          }
            return basicResponse;
        }

        public double calculateFeeAmount (double amount) {
           double profit=Constants.ZERO_PROFIT;
           profit =amount- amount*(Constants.PRESENT_OF_FEE/Constants.PRESENT);
            return profit;

        }


}
