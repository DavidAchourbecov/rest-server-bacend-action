package com.dev.responses;

import com.dev.models.UserDetailsModel;

public class UserDetailsModelResponse extends  BasicResponse{
    private UserDetailsModel userDetailsModel;



    public UserDetailsModelResponse(boolean success, Integer errorCode, UserDetailsModel userDetailsModel) {
        super(success, errorCode);
        this.userDetailsModel = userDetailsModel;
    }

    public UserDetailsModel getUserDetailsModel() {
        return userDetailsModel;
    }

    public void setUserDetailsModel(UserDetailsModel userDetailsModel) {
        this.userDetailsModel = userDetailsModel;
    }
}
