package com.dev.controllers;

import com.dev.models.MainTableModel;
import com.dev.objects.Action;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.MainTableModelResponse;
import com.dev.utils.Errors;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainTableController {
    @Autowired
    private Persist persist;

    @RequestMapping(value = "/get-main-table", method = RequestMethod.GET)
    public BasicResponse getMainTableByToken (String token) {
        BasicResponse basicResponse = null;
        User user = persist.getUserByToken(token);
        if (user != null) {
            List<Product> products = persist.getProductsOpenToActionByUserId(user.getId());
            List<MainTableModel> mainTableModels = new ArrayList<>();
            for (Product product : products) {
                List<Action> actions = persist.getGeneralBidsByProductId(product.getId());
                MainTableModel mainTableModel = new MainTableModel(product, actions, user.getId(), Constants.FIRST_DATA,0);
                mainTableModels.add(mainTableModel);
            }
            basicResponse = new MainTableModelResponse(true, null, mainTableModels);
        } else {
            basicResponse = new BasicResponse(false, Errors.ERROR_NO_SUCH_TOKEN);
        }
        return basicResponse;
    }
}
