package com.dev.controllers;

import com.dev.objects.CreditManagement;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.CreditManagementResponse;
import com.dev.responses.LoginResponse;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.dev.utils.Errors.*;

@RestController
public class LoginController {


    @Autowired
    private Utils utils;

    @Autowired
    private Persist persist;

    @Autowired
    private LifeStatisticsController lifeStatisticsController;

    @RequestMapping(value = "sign-up")
    public BasicResponse signUp (String username, String password,boolean isAdmin){
        BasicResponse basicResponse = new BasicResponse();
        boolean success = false;
        Integer errorCode = null;
        if (username != null) {
            if (password != null) {
                if (utils.isStrongPassword(password)) {
                    User fromDb = persist.getUserByUsername(username);
                    if (fromDb == null) {
                        User toAdd = new User(username, utils.createHash(username, password),isAdmin);

                      int userId=  persist.saveUser(toAdd);
                       toAdd.setId(userId);
                        CreditManagement creditManagement =new CreditManagement(1000,toAdd);
                      persist.createAmountUser(creditManagement);
                        success = true;
                        BasicResponse basicResponse1 = this.lifeStatisticsController.getStatistics();
                        this.lifeStatisticsController.sendUpdatesStatistics(basicResponse1);
                    } else {
                        errorCode = ERROR_USERNAME_ALREADY_EXISTS;
                    }
                } else {
                    errorCode = ERROR_WEAK_PASSWORD;
                }
            } else {
                errorCode = ERROR_MISSING_PASSWORD;
            }
        } else {
            errorCode = ERROR_MISSING_USERNAME;
        }
        basicResponse.setSuccess(success);
        basicResponse.setErrorCode(errorCode);
        return basicResponse;
    }

    @RequestMapping (value = "login")
    public BasicResponse login (String username, String password) {
        BasicResponse basicResponse = new BasicResponse();
        boolean success = false;
        Integer errorCode = null;
        if (username != null) {
            if (password != null) {
                String token = utils.createHash(username, password);
                User fromDb = persist.getUserByUsernameAndToken(username, token);
                if (fromDb != null) {
                    success = true;
                    basicResponse = new LoginResponse(token);
                } else {
                    errorCode = ERROR_WRONG_LOGIN_CREDS;
                }
            } else {
                errorCode = ERROR_MISSING_PASSWORD;
            }
        } else {
            errorCode = ERROR_MISSING_USERNAME;
        }
        basicResponse.setSuccess(success);
        basicResponse.setErrorCode(errorCode);
        return basicResponse;
    }

    @RequestMapping (value = "statist")
    public BasicResponse getStatistics () {
        BasicResponse basicResponse = null;
        if (this.lifeStatisticsController.getStatistics() != null) {
            basicResponse = this.lifeStatisticsController.getStatistics();
        } else {
            basicResponse = new BasicResponse(false, ERROR_STATISTICS);
        }
        return basicResponse;
    }

    @RequestMapping(value = "get-credit" , method = {RequestMethod.GET, RequestMethod.POST})
    public  BasicResponse getCredit (String token){
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            CreditManagement creditManagement = persist.getCreditManagement(user.getId());
            basicResponse = new CreditManagementResponse(true, null, creditManagement);
        } else {
            basicResponse = new BasicResponse(false, ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;

    }
}
