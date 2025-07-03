package com.gpn.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alerts")
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getGasStationBrand() {
        return gasStationBrand;
    }

    public void setGasStationBrand(String gasStationBrand) {
        this.gasStationBrand = gasStationBrand;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public float getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(float expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public Boolean getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(Boolean pushNotification) {
        this.pushNotification = pushNotification;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}