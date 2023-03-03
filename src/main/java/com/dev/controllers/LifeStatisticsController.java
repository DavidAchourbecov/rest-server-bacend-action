package com.dev.controllers;


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

@Controller
public class LifeStatisticsController {
    @Autowired
    private Persist persist;

    private List<SseEmitter> emitterList = new ArrayList<>();
    private Map<String, SseEmitter> emitterMap = new HashMap<>();

   /* @RequestMapping (value = "/sse-statist", method = RequestMethod.GET)
    public SseEmitter handle() {
        SseEmitter sseEmitter = new SseEmitter(10L * 60 * 1000);
        String key = "test";
        this.emitterMap.put(key, sseEmitter);
        return sseEmitter;
    }

    @Scheduled(fixedRate = 1000)
    public void sendUpdates() {
        for (Map.Entry<String, SseEmitter> entry : emitterMap.entrySet()) {
            SseEmitter emitter = entry.getValue();
            try {
                emitter.send("test");
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitterMap.remove(entry.getKey());
            }
        }
    }*/


    @RequestMapping(value = "/sse-statist", method = RequestMethod.GET)
    public SseEmitter handle() {
        SseEmitter sseEmitter = new SseEmitter(10L * 60 * 1000);
        emitterList.add(sseEmitter);
        sseEmitter.onCompletion(() -> emitterList.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitterList.remove(sseEmitter));
        return sseEmitter;
    }

    @Scheduled(fixedRate = 5000)

    public void sendUpdates() {
        for (SseEmitter emitter : emitterList) {
            try {

                emitter.send("test");
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitterList.remove(emitter);
            }
        }
    }




















}
