package com.gpn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;  // Firebase UID

    @Column(nullable = false, unique = true)
    private String userEmail;

    private String fcmToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alert> alerts;

    public User(String id, String userEmail, String fcmToken) {
        this.id = id;
        this.userEmail = userEmail;
        this.fcmToken = fcmToken;
    }
}
