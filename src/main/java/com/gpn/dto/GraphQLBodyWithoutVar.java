package com.gpn.dto;

import lombok.Data;

@Data
public class GraphQLBodyWithoutVar {
    private String operationName;
    private String query;
}
