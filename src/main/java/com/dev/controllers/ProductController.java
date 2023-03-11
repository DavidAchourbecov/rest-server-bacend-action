package com.dev.controllers;

import com.dev.models.ActionModel;
import com.dev.models.MainTableModel;
import com.dev.models.MyProducts;
import com.dev.models.ProductModel;
import com.dev.objects.Action;
import com.dev.objects.CreditManagement;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.MyProductsResponse;
import com.dev.responses.ProductResponse;
import com.dev.utils.Constants;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

import static com.dev.utils.Errors.*;

@RestController
public class ProductController {
    @Autowired
    private Persist persist;
    @Autowired
    private LifeStatisticsController lifeStatisticsController;

    @Autowired
    private LiveUpdatesMainTableController liveUpdatesMainTableController;

    @RequestMapping(value = "add-product", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse addProduct(String token, String productName, String content, String imageLink, double minimumPrice, Boolean openToAction) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            CreditManagement creditManagement = persist.getCreditManagement(user.getId());
            if (creditManagement == null) {
                basicResponse = new BasicResponse(false, ERROR_NO_CREDIT);
                return basicResponse;
            }
            if (creditManagement.getCreditAmount() < minimumPrice) {
                basicResponse = new BasicResponse(false, ERROR_NOT_ENOUGH_CREDIT);
                return basicResponse;
            } else {
                Product product = new Product(productName, content, imageLink, minimumPrice, openToAction, user);
                product.setId(persist.saveProduct(product));
                MainTableModel mainTableModel = new MainTableModel(product, new ArrayList<Action>(), 0,Constants.STATUS_ADD_PRODUCT,0);
                liveUpdatesMainTableController.sendUpdatesMainTable(mainTableModel);

                BasicResponse basicResponse1 = this.lifeStatisticsController.getStatistics();
                this.lifeStatisticsController.sendUpdatesStatistics(basicResponse1);
                creditManagement.setCreditAmount(creditManagement.getCreditAmount() - 2);
                persist.updateCreditManagement(creditManagement);
                User admin = persist.getUserByUsername("admin");
                CreditManagement creditManagementAdmin = persist.getCreditManagement(admin.getId());
                creditManagementAdmin.setCreditAmount(creditManagementAdmin.getCreditAmount() + 2);
                persist.updateCreditManagement(creditManagementAdmin);
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
                List<Action> actions = persist.getActionsByProductId(productId);
                List<Action> myBids = persist.getActionsByProductIdAndUserId(productId, user.getId());
                ProductModel productModel = new ProductModel(product, actions.size(), myBids);
                basicResponse = new ProductResponse(true, null, productModel);
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
                    basicResponse = new BasicResponse(false, ERROR_MIN_3_BIDS);
                    return basicResponse;
                } else {
                    product.setOpenToAction(false);
                    persist.updateProduct(product);
                    MainTableModel mainTableModel = new MainTableModel(product, actions, 0,Constants.STATUS_CLOSE_PRODUCT,0);
                    liveUpdatesMainTableController.sendUpdatesMainTable(mainTableModel);
                    BasicResponse basicResponse1 = this.lifeStatisticsController.getStatistics();
                    this.lifeStatisticsController.sendUpdatesStatistics(basicResponse1);
                    List<Action> actionsWinner = persist.updateWinnerActionsByProductIdMaxAmountLastOffer(productId);
                    Action action = this.getWinnerAction(actionsWinner, productId);
                    action.setWinner(Constants.WINNER);
                    persist.updateAction(action);
                    persist.updateCreditManagementToLoserActionByProductId(productId, true, action.getUserSuggest());
                    User admin = persist.getUserByUsername("admin");
                    CreditManagement creditManagement = persist.getCreditManagement(user.getId());
                    double amount = this.calculateFeeAmount(action.getUserSuggestAmount());
                    creditManagement.setCreditAmount(amount + creditManagement.getCreditAmount());
                    persist.updateCreditManagement(creditManagement);

                    CreditManagement creditManagementAdmin = persist.getCreditManagement(admin.getId());
                    creditManagementAdmin.setCreditAmount(creditManagementAdmin.getCreditAmount() +
                            action.getUserSuggestAmount() - amount);
                    persist.updateCreditManagement(creditManagementAdmin);


                    basicResponse = new BasicResponse(true, null);
                }

            } else {
                basicResponse = new BasicResponse(false, ERROR_NO_SUCH_TOKEN);
            }
        }
        return basicResponse;
    }

    public double calculateFeeAmount(double amount) {
        double profit = Constants.ZERO_PROFIT;
        profit = amount - amount * (Constants.PRESENT_OF_FEE / Constants.PRESENT);
        return profit;

    }

    public Action getWinnerAction(List<Action> actions, int productId) {
        Action action = null;

        if (actions.size() > 1) {
            action = persist.getWinnerActionsByProductIdMaxAmountAndMinDate(productId);

        } else if (actions.size() == 1) {
            action = actions.get(0);
        }


        return action;

    }


    @RequestMapping(value = "get-my-products", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse getMyProducts(String token) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            List<Product> products = persist.getProductsByUserId(user.getId());
            List<MyProducts> myProducts = new ArrayList<>();
            for (Product product : products) {
                double bidMax = 0;
                List<Action> actionsWinner = persist.updateWinnerActionsByProductIdMaxAmountLastOffer(product.getId());
                Action action = this.getWinnerAction(actionsWinner, product.getId());
                if (action != null) {
                    bidMax = action.getUserSuggestAmount();
                }
                MyProducts myProduct = new MyProducts(product, bidMax);
                myProducts.add(myProduct);
            }
            basicResponse = new MyProductsResponse(true, null, myProducts);
        } else {
            basicResponse = new BasicResponse(false, ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }


}
