package com.dev.controllers;

import com.dev.objects.CreditManagement;
import com.dev.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dev.utils.Constants.MINUTE;

@Controller
public class LiveUpdatesCredit {
    @Autowired
    private Persist persist;

    private List<SseEmitter> emitterList = new ArrayList<>();
    private Map<String, SseEmitter> emitterMap = new HashMap<>();

    @RequestMapping(value = "/sse-handler-credit", method = RequestMethod.GET)
    public SseEmitter handle () {
        SseEmitter sseEmitter = new SseEmitter(10L * MINUTE*1000*1000 *1000*1000*1000*1000);
        emitterList.add(sseEmitter);
        sseEmitter.onCompletion(() -> emitterList.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitterList.remove(sseEmitter));
        return sseEmitter;
    }

    public void sendUpdatesCredit(CreditManagement credit) {
        for (SseEmitter emitter : emitterList) {
            try {
                emitter.send(credit);
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitterList.remove(emitter);
            }
        }
    }

}
