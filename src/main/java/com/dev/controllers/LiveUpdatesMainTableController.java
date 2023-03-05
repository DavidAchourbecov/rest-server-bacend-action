package com.dev.controllers;

import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LiveUpdatesMainTableController {
    @Autowired
    private Persist persist;

    private List<SseEmitter> emitterList = new ArrayList<>();
    private Map<String, SseEmitter> emitterMap = new HashMap<>();


    @RequestMapping(value = "/sse-main-table", method = RequestMethod.GET)
    public SseEmitter handleGetMainTableByUserId(String token) {
        SseEmitter sseEmitter = new SseEmitter(10L * 60 * 1000);

        emitterList.add(sseEmitter);
        sseEmitter.onCompletion(() -> emitterList.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitterList.remove(sseEmitter));
        return sseEmitter;
    }

    @Scheduled(fixedRate = 1000)
    public void sendUpdatesMainTable() {
        for (SseEmitter emitter : emitterList) {
            try {
                emitter.send("update");
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitterList.remove(emitter);
            }
        }
    }





}
