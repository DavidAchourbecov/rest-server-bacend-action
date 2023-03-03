package com.dev.objects;

import com.google.gson.JsonObject;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StatisticsHandler {
    private static List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public static void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public static void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public static void sendStatistics(JsonObject statsJson) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(statsJson);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
