package com.gpn.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpn.entity.Alert;
import com.gpn.services.AlertService;
import com.gpn.services.GraphQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GPNController {

    private final GraphQLService graphQLService;
    private final AlertService alertService;

    @Autowired
    public GPNController(GraphQLService graphQLService, AlertService alertService) {
        this.graphQLService = graphQLService;
        this.alertService = alertService;
    }

    @GetMapping(value = "/findByStationId", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findByStationId(@RequestHeader("stationId") int stationId) {
        return graphQLService.findByStationId(stationId);
    }

    @GetMapping(value = "/findByCityOrZipcode", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findByCityOrZipcode(@RequestHeader("search") String search, @RequestHeader("fuel") int fuel, @RequestHeader("maxAge") int maxAge, @RequestHeader("brandId") String brandId) throws JsonProcessingException {
        int parsedBrandId = brandId.isEmpty() ? 1 : Integer.parseInt(brandId); // Handle empty string
        return graphQLService.findByCityOrZipcode(search, fuel, maxAge, parsedBrandId);
    }

    @PostMapping(value = "/createAlert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNotificationTrigger(@RequestBody Alert ALert) {
        alertService.save(ALert);
        return ResponseEntity.ok("Notification Trigger Created Successfully!");
    }

    @GetMapping("/getAlertDetails/{stationId}")
    public ResponseEntity<?> getAlertDetails(@PathVariable int stationId) {
        Alert alert = alertService.findByStationId(stationId);
        return ResponseEntity.ok(alert);
    }

    @PutMapping("/updateAlert")
    public ResponseEntity<?> updateAlert(@RequestBody Alert alert) {
            Alert updatedAlert = alertService.updateAlert(alert);
            return ResponseEntity.ok(updatedAlert);
    }

    @GetMapping(value = "/getBrands", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getBrands() {
        System.out.println("--------------BrandID Fetching -----------");
        return graphQLService.getBrands();
    }

    @GetMapping(value = "/getAlerts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Alert> getAlerts() {
        return alertService.getAlerts();
    }

    @DeleteMapping("/deleteAlert/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable Long id) {
        boolean isDeleted = alertService.deleteAlertById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Alert deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Alert not found");
        }
    }
}

