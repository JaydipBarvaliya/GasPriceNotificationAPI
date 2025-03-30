package com.gpn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {

    private Long id;

    @NotNull(message = "Station ID is required")
    private Integer stationId;

    @NotNull(message = "Fuel type is required")
    private String fuelType;

    @NotNull(message = "Expected price is required")
    private Float expectedPrice;

    private Boolean pushNotification;
    private String line1;
    private String locality;
    private String postalCode;
    private String region;
    private String countryCode;
    private String gasStationBrand;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "FCM Token is required")
    private String fcmToken;

}