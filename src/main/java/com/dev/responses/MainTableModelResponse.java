package com.dev.responses;


import com.dev.models.MainTableModel;

import java.util.List;

public class MainTableModelResponse extends BasicResponse{
    private List<MainTableModel> mainTableModels;

    public MainTableModelResponse(boolean success, Integer errorCode, List<MainTableModel> mainTableModels) {
        super(success, errorCode);
        this.mainTableModels = mainTableModels;
    }

    public List<MainTableModel> getMainTableModels() {
        return mainTableModels;
    }

    public void setMainTableModels(List<MainTableModel> mainTableModels) {
        this.mainTableModels = mainTableModels;
    }

}
