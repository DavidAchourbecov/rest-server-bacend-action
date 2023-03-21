package com.dev.controllers;

import com.dev.models.MainTableModel;
import com.dev.models.MyBidsModel;
import com.dev.objects.Action;
import com.dev.objects.CreditManagement;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.BidResponse;
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
    private LiveUpdatesMainTableController liveUpdatesMainTableController;


    @RequestMapping(value = "add-bid", method = {RequestMethod.POST, RequestMethod.GET})

    public BasicResponse addBid(String token, int productId, double bidAmount) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        int id = 0;
        if (user.getAdmin()){
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_PERMISSION);
            return basicResponse;
        }

        if (user != null) {
            CreditManagement creditManagement = persist.getCreditManagement(user.getId());
            if (creditManagement == null) {
                basicResponse = new BasicResponse(false, Errors.ERROR_NO_CREDIT);
                return basicResponse;
            }
            Product product = persist.getProductById(productId);

            if (product == null) {
                basicResponse = new BasicResponse(false, Errors.ERROR_NO_PRODUCT);
                return basicResponse;
            }

            Product checkUserProduct = persist.getProductByProductIdAndUserId(productId,user.getId());
            if(checkUserProduct != null){
                basicResponse = new BasicResponse(false, Errors.ERROR_USER_IS_OWNER);
                return basicResponse;
            }


            if (!product.getOpenToAction()) {
                basicResponse = new BasicResponse(false, Errors.ERROR_PRODUCT_NOT_OPEN_TO_ACTION);
                return basicResponse;
            }

            if (product.getMinimumPrice() > bidAmount) {
                basicResponse = new BasicResponse(false, Errors.ERROR_BID_AMOUNT);
                return basicResponse;
            }

            if (creditManagement.getCreditAmount() < bidAmount) {
                basicResponse = new BasicResponse(false, Errors.ERROR_NOT_ENOUGH_CREDIT);
                return basicResponse;
            } else {

                Action checkUserAction = persist.getActionByProductIdAndUserIdAndLastOffer(productId, user.getId(), true);
                if (checkUserAction !=null){
                    if (checkUserAction.getUserSuggestAmount() >= bidAmount){
                        basicResponse = new BasicResponse(false, Errors.ERROR_BIG_AMOUNT);
                        return basicResponse;
                    }
                }

                Action actionAction = persist.getActionByProductIdAndUserIdAndLastOffer(productId, user.getId(), true);
                if (actionAction == null) {
                    if (creditManagement.getCreditAmount() < bidAmount + 1) {
                        basicResponse = new BasicResponse(false, Errors.ERROR_NOT_ENOUGH_CREDIT);
                        return basicResponse;
                    }
                    Action action = new Action(user, bidAmount, product, true, Constants.NO_RESULT);
                    id= persist.saveAction(action);
                    if (id == 0) {
                        basicResponse = new BasicResponse(false, Errors.ERROR_SOMETHING_WENT_WRONG);
                        return basicResponse;
                    }

                    creditManagement.setCreditAmount(creditManagement.getCreditAmount() - bidAmount - 1);
                    this.updateCreditManagement(creditManagement);
                    this.paymentSystem();
                    this.sendUpdatesMainTable(productId,product,user,token);
                    basicResponse =new BidResponse(true, null, id,bidAmount);
                } else {
                    if (actionAction.getUserSuggestAmount() < bidAmount) {
                        actionAction.setLastOffer(false);
                        Action action = new Action(user, bidAmount, product, true, Constants.NO_RESULT);
                        persist.updateAction(actionAction);
                       id= persist.saveAction(action);
                        if (id == 0) {
                            basicResponse = new BasicResponse(false, Errors.ERROR_SOMETHING_WENT_WRONG);
                            return basicResponse;
                        }

                        double amount = bidAmount - actionAction.getUserSuggestAmount();
                        creditManagement.setCreditAmount(creditManagement.getCreditAmount() - amount - 1);
                        this.updateCreditManagement(creditManagement);
                        this.paymentSystem();
                        this.sendUpdatesMainTable(productId,product,user,token);
                        basicResponse =new BidResponse(true, null, id,bidAmount);
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

    public void sendUpdatesMainTable(int productId,Product product,User user,String token) {
        List<Action> actionList = persist.getActionsByProductId(productId);
        MainTableModel mainTableModel = new MainTableModel(product, actionList,user.getId(),Constants.STATUS_ADD_BID,token,user.getUsername());
        this.liveUpdatesMainTableController.sendUpdatesMainTable(mainTableModel);

    }



}
