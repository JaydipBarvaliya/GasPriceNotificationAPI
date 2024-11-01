package com.gpn.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private String zipCode;
    private String area;
    private String brand;
    private double priceDrop;
}
