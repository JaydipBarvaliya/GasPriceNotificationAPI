package com.gpn.entity;

import lombok.Data;

@Data
public class GasNotificationRequest {
    private String zipCode;
    private String area;
    private String brand;
    private double priceDrop;
}
