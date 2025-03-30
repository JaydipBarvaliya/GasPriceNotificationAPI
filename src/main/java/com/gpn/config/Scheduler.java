package com.gpn.config;

import com.gpn.entity.Alert;
import com.gpn.repository.AlertRepository;
import com.gpn.service.FCMService;
import com.gpn.service.GraphQLService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {

    private final AlertRepository alertRepository;
    private final GraphQLService graphQLService;
    private final FCMService fcmService;

    public Scheduler(AlertRepository alertRepository, GraphQLService graphQLService, FCMService fcmService) {
        this.alertRepository = alertRepository;
        this.graphQLService = graphQLService;
        this.fcmService = fcmService;
    }

//    @Scheduled(fixedRate = 600000) // Runs every 10 minutes
    public void checkPriceAlerts() {
        List<Alert> alerts = alertRepository.findAll(); // Fetch all active alerts

        for (Alert alert : alerts) {
            try {
                double currentPrice = Double.parseDouble(graphQLService.findByStationId(alert.getStationId())); // Fetch current gas price

                if (currentPrice <= alert.getExpectedPrice()) {
                    String fcmToken = alert.getUser().getFcmToken();
                    if (fcmToken != null && !fcmToken.isEmpty()) {
                        //TODO: add the following line in the below statement :: alert.getLocation()
                        fcmService.sendNotification(fcmToken, "⛽ Gas Price Alert!", "Gas price at "  + " is now $" + currentPrice);
                    }
                }
            } catch (Exception e) {
                System.err.printf("Error processing alert for station %s: %s%n", alert.getGasStationBrand(), e.getMessage());
                e.printStackTrace(); // Print full stack trace for debugging
            }
        }
    }
}
