package com.dev.controllers;

import com.dev.models.MainTableModel;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

import static com.dev.utils.Constants.MINUTE;

@Controller
public class LiveUpdatesMainTableController {
    @Autowired
    private Persist persist;

    private List<SseEmitter> emitterList = new ArrayList<>();


    @RequestMapping (value = "/sse-handler-main-table", method = RequestMethod.GET)
    public SseEmitter handle () {
        SseEmitter sseEmitter = new SseEmitter(10L * MINUTE);
        emitterList.add(sseEmitter);
        sseEmitter.onCompletion(() -> emitterList.remove(sseEmitter));
       sseEmitter.onTimeout(() -> emitterList.remove(sseEmitter));
        return sseEmitter;
    }


    public void sendUpdatesMainTable(MainTableModel mainTableModel) {


        Iterator<SseEmitter> iterator = emitterList.iterator();
        System.out.println("emitterList.size() = " + emitterList.size());
        while (iterator.hasNext()) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(mainTableModel);
            } catch (Exception e) {
                emitter.completeWithError(e);
                iterator.remove();
            }
        }
    }







}
