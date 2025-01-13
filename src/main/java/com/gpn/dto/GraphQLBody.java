package com.gpn.dto;

import lombok.Data;

@Data
public class GraphQLBody {
    private String operationName;
    private String query;
    private GraphQLVars variables;
}
