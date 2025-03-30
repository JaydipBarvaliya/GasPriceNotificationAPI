package com.gpn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int stationId;
    private String gasStationBrand;
    private String fuelType;
    private float expectedPrice;
    private Boolean pushNotification;
    private String line1;
    private String locality;
    private String postalCode;
    private String region;
    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)  // This links the alert to a specific user
    private User user;
}