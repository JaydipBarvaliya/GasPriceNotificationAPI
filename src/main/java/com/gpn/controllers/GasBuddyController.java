package com.gpn.controllers;

import com.gpn.services.GasBuddyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GasBuddyController {

    private final GasBuddyService gasBuddyService;

    @Autowired
    public GasBuddyController(GasBuddyService gasBuddyService) {
        this.gasBuddyService = gasBuddyService;
    }

    @GetMapping(value = "/findByStationId/{stationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStationInfo(@PathVariable String stationId) {
        return gasBuddyService.findByStationId(stationId);
    }
}

