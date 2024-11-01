package com.gpn.controllers;

import com.gpn.dto.NotificationDTO;
import com.gpn.entity.Station;
import com.gpn.services.GasStationService;
import com.gpn.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GPNController {

    private final GasStationService gasStationService;
    private final StationService stationService;

    @Autowired
    public GPNController(GasStationService gasStationService, StationService stationService) {
        this.gasStationService = gasStationService;
        this.stationService = stationService;
    }

    @GetMapping(value = "/findByStationId/{stationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStationInfo(@PathVariable String stationId) {
        return gasStationService.findByStationId(stationId);
    }

    @PostMapping(value = "/createNotificationTrigger", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNotificationTrigger(@RequestBody NotificationDTO request) {
        // Handle the form data and trigger the notification logic here
        System.out.println("Received request: " + request);

        // Assume trigger is created successfully
        return ResponseEntity.ok("Notification Trigger Created Successfully!");
    }

    @GetMapping(value = "/getAllGasStations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @PostMapping(value = "/addStation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Station addStation(@RequestBody Station station) {
        return stationService.addStation(station);
    }

}

