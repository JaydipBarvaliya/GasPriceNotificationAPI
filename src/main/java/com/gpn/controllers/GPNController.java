package com.gpn.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpn.entity.Alert;
import com.gpn.services.AlertService;
import com.gpn.services.GraphQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GPNController {

    private static final Logger logger = LoggerFactory.getLogger(GPNController.class);

    private final GraphQLService graphQLService;
    private final AlertService alertService;

    @Autowired
    public GPNController(GraphQLService graphQLService, AlertService alertService) {
        this.graphQLService = graphQLService;
        this.alertService = alertService;
    }

    @GetMapping(value = "/findByStationId", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findByStationId(@RequestHeader("stationId") int stationId) {
        logger.info("Called findByStationId with stationId: {}", stationId);
        return graphQLService.findByStationId(stationId);
    }

    @GetMapping(value = "/findByCityOrZipcode", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findByCityOrZipcode(
            @RequestHeader("search") String search,
            @RequestHeader("fuel") int fuel,
            @RequestHeader("maxAge") int maxAge,
            @RequestHeader("brandId") String brandId
    ) throws JsonProcessingException {
        logger.info("Called findByCityOrZipcode with search: {}, fuel: {}, maxAge: {}, brandId: {}",
                search, fuel, maxAge, brandId);

        int parsedBrandId = brandId.isEmpty() ? 1 : Integer.parseInt(brandId);
        return graphQLService.findByCityOrZipcode(search, fuel, maxAge, parsedBrandId);
    }

    @PostMapping(value = "/createAlert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNotificationTrigger(@RequestBody Alert alert) {
        logger.info("Called createNotificationTrigger with alert: {}", alert);
        alertService.save(alert);
        return ResponseEntity.ok("Notification Trigger Created Successfully!");
    }

    @GetMapping("/getAlertDetails/{stationId}")
    public ResponseEntity<?> getAlertDetails(@PathVariable int stationId) {
        logger.info("Called getAlertDetails with stationId: {}", stationId);
        Alert alert = alertService.findByStationId(stationId);
        return ResponseEntity.ok(alert);
    }

    @PutMapping("/updateAlert")
    public ResponseEntity<?> updateAlert(@RequestBody Alert alert) {
        logger.info("Called updateAlert with alert: {}", alert);
        Alert updatedAlert = alertService.updateAlert(alert);
        return ResponseEntity.ok(updatedAlert);
    }

    @GetMapping(value = "/getBrands", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getBrands() {
        logger.info("Called getBrands endpoint");
        return graphQLService.getBrands();
    }

    @GetMapping(value = "/getAlerts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Alert> getAlerts() {
        logger.info("Called getAlerts endpoint");
        return alertService.getAlerts();
    }

    @DeleteMapping("/deleteAlert/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable Long id) {
        logger.info("Called deleteAlert with id: {}", id);
        boolean isDeleted = alertService.deleteAlertById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Alert deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Alert not found");
        }
    }

    @DeleteMapping("/deleteAllAlerts")
    public ResponseEntity<String> deleteAllAlert() {
        logger.info("Called deleteAllAlert endpoint");
        boolean isDeleted = alertService.deleteAllAlert();
        if (isDeleted) {
            return ResponseEntity.ok("All the Alerts have been deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Alerts Deletion failed");
        }
    }
}
