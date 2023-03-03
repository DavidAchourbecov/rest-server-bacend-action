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
    /*@GetMapping("/live-statistics")
    public SseEmitter liveStatistics() {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(30)); // Set timeout to 30 minutes

        // Create a new thread to periodically update the live statistics
        Thread thread = new Thread(() -> {
            while (!emitter.isClosed()) {
                try {
                    // Fetch the live statistics from the system
                    int numUsers = userService.getNumUsers();
                    int numOpenTenders = tenderService.getNumOpenTenders();
                    int numClosedTenders = tenderService.getNumClosedTenders();
                    int numOpenBids = bidService.getNumOpenBids();
                    int numClosedBids = bidService.getNumClosedBids();

                    // Create a message with the live statistics
                    String message = String.format("Users: %d, Open Tenders: %d, Closed Tenders: %d, Open Bids: %d, Closed Bids: %d",
                            numUsers, numOpenTenders, numClosedTenders, numOpenBids, numClosedBids);

                    // Send the message to the client using the SseEmitter
                    emitter.send(SseEmitter.event().data(message));

                    // Wait for 5 seconds before sending the next message
                    Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                } catch (Exception e) {
                    emitter.completeWithError(e);
                    return;
                }
            }
        });

        thread.start();

        return emitter;
    }*/



}
