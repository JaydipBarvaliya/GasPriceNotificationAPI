package com.gpn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int stationId;
    private int fuelType;
    private float expectedPrice;
    private Boolean pushNotification;
    private String email;
    private String line1;
    private String locality;
    private String postalCode;
    private String region;
    private String name;
}