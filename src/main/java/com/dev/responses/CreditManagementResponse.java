package com.dev.responses;

import com.dev.objects.CreditManagement;

public class CreditManagementResponse extends BasicResponse{
    private CreditManagement creditManagement;

    public CreditManagementResponse(boolean success, Integer errorCode, CreditManagement creditManagement) {
        super(success, errorCode);
        this.creditManagement = creditManagement;
    }

    public CreditManagement getCreditManagement() {
        return creditManagement;
    }

    public void setCreditManagement(CreditManagement creditManagement) {
        this.creditManagement = creditManagement;
    }
}
