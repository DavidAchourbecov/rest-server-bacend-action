package com.dev.controllers;

import com.dev.models.MyBidsModel;
import com.dev.objects.Action;
import com.dev.objects.CreditManagement;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.MyBidsModelResponse;
import com.dev.utils.Constants;
import com.dev.utils.Errors;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BidController {
    @Autowired
    private Persist persist;
    @Autowired
    private LifeStatisticsController lifeStatisticsController;

    @RequestMapping(value = "add-bid", method = {RequestMethod.POST, RequestMethod.GET})

    public BasicResponse addBid(String token, int productId, double bidAmount) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            CreditManagement creditManagement = persist.getCreditManagement(user.getId());
            if (creditManagement == null) {
                basicResponse = new BasicResponse(false, Errors.ERROR_NO_CREDIT);
                return basicResponse;
            }
            if (creditManagement.getCreditAmount() < bidAmount) {
                basicResponse = new BasicResponse(false, Errors.ERROR_NOT_ENOUGH_CREDIT);
                return basicResponse;
            } else {
                Product product = persist.getProductById(productId);
                if (product == null) {
                    basicResponse = new BasicResponse(false, Errors.ERROR_NO_PRODUCT);
                    return basicResponse;
                }
                if (product.getMinimumPrice() > bidAmount) {
                    basicResponse = new BasicResponse(false, Errors.ERROR_BID_AMOUNT);
                    return basicResponse;
                }
                if (!product.getOpenToAction()) {
                    basicResponse = new BasicResponse(false, Errors.ERROR_PRODUCT_NOT_OPEN_TO_ACTION);
                    return basicResponse;
                }
                Action actionAction = persist.getActionByProductIdAndUserIdAndLastOffer(productId, user.getId(), true);
                BasicResponse basicResponse1 = this.lifeStatisticsController.getStatistics();
                this.lifeStatisticsController.sendUpdatesStatistics(basicResponse1);
                if (actionAction == null) {
                    Action action = new Action(user, bidAmount, product, true, Constants.NO_RESULT);
                    persist.saveAction(action);
                    creditManagement.setCreditAmount(creditManagement.getCreditAmount() - bidAmount - 1);
                    this.updateCreditManagement(creditManagement);
                    this.paymentSystem();
                    basicResponse = new BasicResponse(true, null);
                } else {
                    if (actionAction.getUserSuggestAmount() < bidAmount) {
                        actionAction.setLastOffer(false);
                        Action action = new Action(user, bidAmount, product, true, Constants.NO_RESULT);
                        persist.updateAction(actionAction);
                        persist.saveAction(action);
                        double amount = bidAmount - actionAction.getUserSuggestAmount();
                        creditManagement.setCreditAmount(creditManagement.getCreditAmount() - amount - 1);
                        this.updateCreditManagement(creditManagement);
                        this.paymentSystem();
                        basicResponse = new BasicResponse(true, null);
                    } else {
                        basicResponse = new BasicResponse(false, Errors.ERROR_BID_AMOUNT);
                    }

                }

            }
        } else {
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }

    public void updateCreditManagement(CreditManagement creditManagement) {
        persist.updateCreditManagement(creditManagement);
    }

    public void paymentSystem() {
        User admin = persist.getUserByUsername("admin");
        CreditManagement creditManagementAdmin = persist.getCreditManagement(admin.getId());
        creditManagementAdmin.setCreditAmount(creditManagementAdmin.getCreditAmount() + 1);
        this.updateCreditManagement(creditManagementAdmin);

    }

    @RequestMapping(value = "get-my-bids", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse getMyBids(String token) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            List<Action> actionList = persist.getActionsByUserId(user.getId());
            List <MyBidsModel> myBidsModelList = new ArrayList<>();
            for (Action action : actionList) {
                MyBidsModel myBidsModel = new MyBidsModel(action);
                myBidsModelList.add(myBidsModel);
            }
            basicResponse = new MyBidsModelResponse(true, null, myBidsModelList);

        } else {
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }





}
