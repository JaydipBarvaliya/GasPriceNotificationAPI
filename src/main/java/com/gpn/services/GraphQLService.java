package com.gpn.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gpn.dto.GraphQLBody;
import com.gpn.dto.GraphQLVars;
import com.gpn.entity.Alert;
import com.gpn.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GraphQLService {

    private final WebClient webClient;
    private final AlertRepository alertRepository;

    public GraphQLService(WebClient.Builder webClientBuilder, AlertRepository alertRepository) {
        this.webClient = webClientBuilder.baseUrl("https://www.gasbuddy.com").build();
        this.alertRepository = alertRepository;
    }

    public String findByStationId(int stationId) {
        String query = "query GetStation($id: ID!) { station(id: $id) { prices { credit { postedTime price } } } }";

        return webClient.post()
                .uri("/graphql")
                .header("Content-Type", "application/json")
                .bodyValue(formFindByStationIdRequestBodyXYZ(query, stationId, "GetStation")) // Serialize the GraphQLRequest object to JSON
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String findByCityOrZipcode(String search, int fuel, int maxAge, int brandId) throws JsonProcessingException {

        String query = "query LocationBySearchTerm($search: String, $fuel: Int, $maxAge: Int, $brandId: Int) {\n" +
                "                locationBySearchTerm(search: $search) {\n" +
                "                    countryCode\n" +
                "                    displayName\n" +
                "                    latitude\n" +
                "                    longitude\n" +
                "                    regionCode\n" +
                "                    stations(brandId: $brandId, fuel: $fuel, maxAge: $maxAge) {\n" +
                "                        count\n" +
                "                        results {\n" +
                "                            id\n" +
                "                            name\n" +
                "                            address {\n" +
                "                                line1\n" +
                "                                locality\n" +
                "                                postalCode\n" +
                "                                region\n" +
                "                            }\n" +
                "                            prices {\n" +
                "                                cash {\n" +
                "                                    price\n" +
                "                                    formattedPrice\n" +
                "                                }\n" +
                "                                credit {\n" +
                "                                    price\n" +
                "                                    formattedPrice\n" +
                "                                }\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }";

        String listOfGasStations = webClient.post()
                .uri("/graphql")
                .header("Content-Type", "application/json")
                .bodyValue(formFindByZipcodeRequestBodyXYZ(query, search, fuel, maxAge, brandId, "LocationBySearchTerm")) // Serialize the GraphQLRequest object to JSON
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<String> triggers = alertRepository.findAllStationIds();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(listOfGasStations);
        JsonNode resultsNode = rootNode.at("/data/locationBySearchTerm/stations/results");

        if (resultsNode.isArray()) {
            ArrayNode resultsArray = (ArrayNode) resultsNode;
            for (JsonNode gasStation : resultsArray) {
                if(triggers.contains(gasStation.get("id").asText())){
                    ((ObjectNode) gasStation).put("hasTriggerCreated", true);
                }else{
                    ((ObjectNode) gasStation).put("hasTriggerCreated", false);
                }

            }

        }

        // Convert the modified JSON back to a string
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    public String getBrands(){
        String query = "query Brands {\n" +
                "    brands {\n" +
                "      brandId\n" +
                "      name\n" +
                "    }\n" +
                "  }";


        String listOfBrands = webClient.post()
                .uri("/graphql")
                .header("Content-Type", "application/json")
                .bodyValue(formFetchAllBrandsXYZ(query, "Brands")) // Serialize the GraphQLRequest object to JSON
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return listOfBrands;
    }

    public GraphQLBody formFindByZipcodeRequestBodyXYZ(String query, String search, int fuel, int maxAge, int brandId, String operationName) {

        GraphQLBody body = new GraphQLBody();
        body.setOperationName(operationName);
        body.setQuery(query);

        GraphQLVars vars = new GraphQLVars();
        vars.setSearch(search);
        vars.setFuel(fuel);
        vars.setMaxAge(maxAge);
        if(brandId != 1){
            vars.setBrandId(brandId);
        }
        body.setVariables(vars);
        return body;
    }

    public GraphQLBody formFindByStationIdRequestBodyXYZ(String query, int stationId, String operationName) {

        GraphQLBody body = new GraphQLBody();
        body.setOperationName(operationName);
        body.setQuery(query);

        GraphQLVars vars = new GraphQLVars();
        vars.setId(stationId);

        body.setVariables(vars);
        return body;
    }

    public GraphQLBody formFetchAllBrandsXYZ(String query, String operationName) {

        GraphQLBody body = new GraphQLBody();
        body.setOperationName(operationName);
        body.setQuery(query);

        body.setVariables(new GraphQLVars());
        return body;
    }


}