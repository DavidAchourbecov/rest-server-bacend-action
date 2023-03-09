package com.dev.controllers;


import com.dev.models.MainTableModel;
import com.dev.models.UserDetailsModel;
import com.dev.objects.Action;
import com.dev.objects.CreditManagement;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.responses.AllUsersResponse;
import com.dev.responses.BasicResponse;
import com.dev.responses.MainTableModelResponse;
import com.dev.responses.UserDetailsModelResponse;
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
public class ManageController {

    @Autowired
    private Persist persist;

    @RequestMapping (value = "get-all-users", method = RequestMethod.GET)
    public BasicResponse getAllUsers (String token) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            if (user.getAdmin()) {
                List<User> users = persist.getAllUsers();
                basicResponse = new AllUsersResponse(true, null, users);

            } else {
                basicResponse = new BasicResponse(false, Errors.ERROR_NO_PERMISSION);
            }
        } else {
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }

    @RequestMapping (value = "get-user", method = RequestMethod.GET)
    public BasicResponse getUserDetails (int userId){
        BasicResponse basicResponse = null;
        User user = persist.getUserById(userId);
        if (user != null) {
            CreditManagement creditManagement = persist.getCreditManagement(userId);
            List <Product> products = persist.getMyProductsOpenToActionByUserId(userId);
            UserDetailsModel userDetailsModel = new UserDetailsModel(creditManagement, products);
            basicResponse = new UserDetailsModelResponse(true, null, userDetailsModel);
        } else {
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_SUCH_USER);
        }
        return basicResponse;
    }

    @RequestMapping (value = "update-credit-amount-user", method = RequestMethod.GET)
    public BasicResponse updateCreditAmountUser(double amount, int userId){
        BasicResponse basicResponse = null;
        User user = persist.getUserById(userId);
        if (user != null) {
            CreditManagement creditManagement = persist.getCreditManagement(userId);
            creditManagement.setCreditAmount(amount);
            persist.updateCreditManagement(creditManagement);
            basicResponse = new BasicResponse(true, null);
        } else {
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_SUCH_USER);
        }
        return basicResponse;
    }

    @RequestMapping (value = "get-table", method = RequestMethod.GET)

    public BasicResponse getTable (String token){
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            List <Product> products = persist.getProductsOpenToAction();
            List<MainTableModel> mainTable = new ArrayList<>();
            for (Product product : products) {
                List<Action> actions = persist.getGeneralBidsByProductId(product.getId());
                MainTableModel mainTableModel = new MainTableModel(product, actions, 0 , Constants.FIRST_DATA,0);
                mainTable.add(mainTableModel);
            }
            basicResponse = new MainTableModelResponse(true, null, mainTable);


        } else {
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }




}
