package com.dev.controllers;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Controller
public class LifeStatisticsController {
    //private SseEmitter emitter = new SseEmitter();


    @Autowired
    private Persist persist;


    /*@RequestMapping(value = "/get-stat", method = RequestMethod.GET)
    public SseEmitter getStats() {

        return emitter;
    }*/
/*
    @Scheduled(fixedRate = 5000) // Update stats every 5 seconds
    public void updateStats() {
        // Fetch stats from database or other sources


        int numUsers = persist.getAllUsers().size();
        int numOpenTenders=  persist.getAllOpenOrCloseTenders(true).size();
        int numClosedTenders = persist.getAllOpenOrCloseTenders(false).size();
        int numOpenBids = persist.getAllOpenOrCloseActions(true).size();
        int numClosedBids = persist.getAllOpenOrCloseActions(false).size();


        // Create a JSON object with the updated stats
        JsonObject statsJson = new JsonObject();
        statsJson.addProperty("numUsers", numUsers);
        statsJson.addProperty("numOpenTenders", numOpenTenders);
       statsJson.addProperty("numClosedTenders", numClosedTenders);
        statsJson.addProperty("numOpenBids", numOpenBids);
        statsJson.addProperty("numClosedBids", numClosedBids);
        // Send the updated stats to all SSE clients

            try {
                emitter.send(SseEmitter.event().data(statsJson.toString()));
                //emitter.complete();
            } catch (IOException e) {
                 // Remove the SSE client if it's no longer reachable
            }
        }*/


    @GetMapping("/statistics")

    public JsonObject getStatistics() {
        int numUsers = persist.getAllUsers().size();
        int numOpenTenders=  persist.getAllOpenOrCloseTenders(true).size();
        int numClosedTenders = persist.getAllOpenOrCloseTenders(false).size();
        int numOpenBids = persist.getAllOpenOrCloseActions(true).size();
        int numClosedBids = persist.getAllOpenOrCloseActions(false).size();


        // Create a JSON object with the updated stats
        JsonObject statsJson = new JsonObject();
        statsJson.addProperty("numUsers", numUsers);
        statsJson.addProperty("numOpenTenders", numOpenTenders);
        statsJson.addProperty("numClosedTenders", numClosedTenders);
        statsJson.addProperty("numOpenBids", numOpenBids);
        statsJson.addProperty("numClosedBids", numClosedBids);
        return statsJson;
    }




    @GetMapping("/statistics/stream")

    public SseEmitter streamStatistics() {
        SseEmitter emitter = new SseEmitter();
        StatisticsHandler.addEmitter(emitter);
        emitter.onCompletion(() -> StatisticsHandler.removeEmitter(emitter));
        emitter.onTimeout(() -> StatisticsHandler.removeEmitter(emitter));
        return emitter;
    }
    }

