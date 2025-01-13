package com.gpn.dto;

import lombok.Data;

@Data
public class GraphQLVars {
    private int id;
    private String search;
    private int fuel;
    private int maxAge;
    private int brandId;
}
