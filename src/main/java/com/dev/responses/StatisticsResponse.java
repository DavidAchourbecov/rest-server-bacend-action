package com.dev.responses;

public class StatisticsResponse extends BasicResponse {
    private int numUsers;
    private int numOpenTenders;
    private int numClosedTenders;
    private int numOpenBids;
    private int numClosedBids;


    public StatisticsResponse(boolean success, Integer errorCode, int numUsers, int numOpenTenders, int numClosedTenders, int numOpenBids, int numClosedBids) {
        super(success, errorCode);
        this.numUsers = numUsers;
        this.numOpenTenders = numOpenTenders;
        this.numClosedTenders = numClosedTenders;
        this.numOpenBids = numOpenBids;
        this.numClosedBids = numClosedBids;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public int getNumOpenTenders() {
        return numOpenTenders;
    }

    public void setNumOpenTenders(int numOpenTenders) {
        this.numOpenTenders = numOpenTenders;
    }

    public int getNumClosedTenders() {
        return numClosedTenders;
    }

    public void setNumClosedTenders(int numClosedTenders) {
        this.numClosedTenders = numClosedTenders;
    }

    public int getNumOpenBids() {
        return numOpenBids;
    }

    public void setNumOpenBids(int numOpenBids) {
        this.numOpenBids = numOpenBids;
    }

    public int getNumClosedBids() {
        return numClosedBids;
    }

    public void setNumClosedBids(int numClosedBids) {
        this.numClosedBids = numClosedBids;
    }
}
