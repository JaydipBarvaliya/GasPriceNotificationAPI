package com.gpn.util;

import com.gpn.dto.GraphQLRequestDTO;
import com.gpn.dto.GraphQLVariableDTO;
import org.springframework.stereotype.Component;

@Component
public class GasUtil {

    public static GraphQLRequestDTO createRequestBody(String id, String query) {
        GraphQLRequestDTO request = new GraphQLRequestDTO();
        request.setOperationName("GetStation");
        request.setQuery(query);

        GraphQLVariableDTO variables = new GraphQLVariableDTO();
        variables.setId(id);
        request.setVariables(variables);

        return request;
    }
}
