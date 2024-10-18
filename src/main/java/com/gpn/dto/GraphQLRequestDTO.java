package com.gpn.dto;

import lombok.Data;

@Data
public class GraphQLRequestDTO {
    private String operationName;
    private String query;
    private GraphQLVariableDTO variables;

}