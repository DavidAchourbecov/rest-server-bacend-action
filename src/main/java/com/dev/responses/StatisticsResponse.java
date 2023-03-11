package com.dev.responses;

import com.dev.objects.Statistics;

public class StatisticsResponse extends BasicResponse {
   private Statistics statistics;

    public StatisticsResponse(boolean success, Integer errorCode, Statistics statistics) {
        super(success, errorCode);
        this.statistics = statistics;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
