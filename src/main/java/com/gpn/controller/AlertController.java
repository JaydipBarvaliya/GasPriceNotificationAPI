package com.gpn.controller;

import com.gpn.dto.AlertDTO;
import com.gpn.entity.Alert;
import com.gpn.entity.User;
import com.gpn.exception.ResourceNotFoundException;
import com.gpn.mapper.AlertMapper;
import com.gpn.repository.UserRepository;
import com.gpn.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final AlertService alertService;
    private final UserRepository userRepository;
    private final AlertMapper alertMapper;

    @Autowired
    public AlertController(AlertService alertService, UserRepository userRepository, AlertMapper alertMapper) {
        this.alertService = alertService;
        this.userRepository = userRepository;
        this.alertMapper = alertMapper;
    }

    @PostMapping(value = "/createAlert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createAlert(@RequestBody AlertDTO alertDTO) {

        logger.info("Called createAlert with alertDTO: {}", alertDTO);

        // Fetch user from repository
        User user = userRepository.findById(alertDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + alertDTO.getUserId()));

        Alert alert = alertMapper.toAlert(alertDTO, user);

        alertService.save(alert);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification Trigger Created Successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAlertDetails/{stationId}")
    public ResponseEntity<?> getAlertByStationId(@PathVariable int stationId) {
        logger.info("Called getAlertDetails with stationId: {}", stationId);
        Alert alert = alertService.findAlertByStationId(stationId);
        return ResponseEntity.ok(alert);
    }

    @GetMapping(value = "/getAlerts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AlertDTO> getAlerts() {
        logger.info("Called getAlerts endpoint");
        return alertService.getAlerts().stream()
                .map(alertMapper::toAlertDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/updateAlert")
    public ResponseEntity<List<AlertDTO>> updateAlert(@RequestBody Alert alert) {
        logger.info("Called updateAlert with alert: {}", alert);

        Alert updatedAlert = alertService.updateAlert(alert);

        if (updatedAlert != null) {
            List<AlertDTO> updatedAlerts = alertService.getAlerts().stream()
                    .map(alertMapper::toAlertDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(updatedAlerts);
        } else {
            return ResponseEntity.status(404).body(null); // Return 404 if update fails
        }
    }

    @DeleteMapping("/deleteAlert/{id}")
    public ResponseEntity<List<AlertDTO>> deleteAlert(@PathVariable Long id) {
        logger.info("Called deleteAlert with id: {}", id);

        boolean isDeleted = alertService.deleteAlertById(id);

        if (isDeleted) {
            List<AlertDTO> remainingAlerts = alertService.getAlerts().stream()
                    .map(alertMapper::toAlertDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(remainingAlerts);
        } else {
            return ResponseEntity.status(404).body(null); // Return 404 if alert not found
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
