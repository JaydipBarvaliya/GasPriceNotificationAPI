package com.gpn.controllers;

import com.gpn.entity.GasNotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class GasNotificationController {

    @PostMapping
    public ResponseEntity<String> createNotificationTrigger(@RequestBody GasNotificationRequest request) {
        // Handle the form data and trigger the notification logic here
        System.out.println("Received request: " + request);

        // Assume trigger is created successfully
        return ResponseEntity.ok("Notification Trigger Created Successfully!");
    }
}
