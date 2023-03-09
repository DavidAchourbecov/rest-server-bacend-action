package com.dev.controllers;


import com.dev.responses.BasicResponse;
import com.dev.responses.StatisticsResponse;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static com.dev.utils.Constants.MINUTE;

@Controller
public class LifeStatisticsController {
    @Autowired
    private Persist persist;

    private List<SseEmitter> emitterList = new ArrayList<>();
    private Map<String, SseEmitter> emitterMap = new HashMap<>();



    @RequestMapping(value = "/sse-statist", method = RequestMethod.GET)
    public SseEmitter handle(int id) {
        SseEmitter sseEmitter = new SseEmitter(10L * MINUTE);
        emitterList.add(sseEmitter);
        sseEmitter.onCompletion(() -> emitterList.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitterList.remove(sseEmitter));
        return sseEmitter;
    }



    public void sendUpdatesStatistics(BasicResponse response) {
        for (SseEmitter emitter : emitterList) {
            try {
                emitter.send(response);
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitterList.remove(emitter);
            }
        }
    }


    public BasicResponse getStatistics() {
        BasicResponse response = null;
        int usersCount = persist.getAllUsers().size();
        int numOpenTenders = persist.getAllOpenOrCloseTrades(true).size();
        int numCloseTenders = persist.getAllOpenOrCloseTrades(false).size();
        int numOpenBids = persist.getAllOpenOrCloseActions(true).size();
        int numCloseBids = persist.getAllOpenOrCloseActions(false).size();
        response = new StatisticsResponse(true,null,usersCount,numOpenTenders,numCloseTenders,numOpenBids,numCloseBids);
        return response;
    }


   /* public BasicResponse getMainTableByUserId(String token) {


    }*/




















}
