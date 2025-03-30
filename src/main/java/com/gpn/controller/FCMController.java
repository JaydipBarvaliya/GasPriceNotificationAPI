package com.gpn.controller;

import com.gpn.entity.User;
import com.gpn.repository.UserRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class FCMController {

    private static final Logger logger = LoggerFactory.getLogger(FCMController.class);
    private final UserRepository userRepository;

    public FCMController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<String> registerToken(@RequestBody String payload) {

        logger.info("Called registerToken with payload: {}", payload);
        try {
            JSONObject json = new JSONObject(payload);
            String userId = json.getString("userId");
            String email = json.getString("email");
            String token = json.getString("fcmToken");

            // ✅ Check if user exists, if not, create a new user
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                user = new User(userId, email, token); // Create new user
            } else {
                user.setFcmToken(token); // Update token for existing user
            }

            userRepository.save(user);
            return ResponseEntity.ok("✅ Token registered successfully!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Invalid request format");
        }
    }
}
