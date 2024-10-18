package com.gpn.controllers;

import com.gpn.entity.Station;
import com.gpn.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @PostMapping
    public Station addStation(@RequestBody Station station) {
        return stationService.addStation(station);
    }
}

