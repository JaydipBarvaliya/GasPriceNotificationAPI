package com.gpn.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gpn.dto.GraphQLBody;
import com.gpn.dto.GraphQLBodyWithoutVar;
import com.gpn.dto.GraphQLVars;
import com.gpn.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

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
                .bodyValue(formFindByStationIdGraphQLBody(query, stationId, "GetStation")) // Serialize the GraphQLRequest object to JSON
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public GraphQLBody formFindByStationIdGraphQLBody(String query, int stationId, String operationName) {

        GraphQLBody body = new GraphQLBody();
        body.setOperationName(operationName);
        body.setQuery(query);

        GraphQLVars vars = new GraphQLVars();
        vars.setId(stationId);

        body.setVariables(vars);
        return body;
    }

    public String findByCityOrZipcode(String search, int fuel, int maxAge, int brandId) throws JsonProcessingException {

        String query = """
                query LocationBySearchTerm($search: String, $fuel: Int, $maxAge: Int, $brandId: Int) {
                                locationBySearchTerm(search: $search) {
                                    countryCode
                                    displayName
                                    latitude
                                    longitude
                                    regionCode
                                    stations(brandId: $brandId, fuel: $fuel, maxAge: $maxAge) {
                                        count
                                        results {
                                            id
                                            name
                                            address {
                                                line1
                                                locality
                                                postalCode
                                                region
                                            }
                                            prices {
                                                cash {
                                                    price
                                                    formattedPrice
                                                }
                                                credit {
                                                    price
                                                    formattedPrice
                                                }
                                            }
                                        }
                                    }
                                }
                            }""";

        String listOfGasStations = webClient.post()
                .uri("/graphql")
                .header("Content-Type", "application/json")
                .bodyValue(formFindByZipcodeRequestGraphQLBody(query, search, fuel, maxAge, brandId, "LocationBySearchTerm")) // Serialize the GraphQLRequest object to JSON
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
                if (triggers.contains(gasStation.get("id").asText())) {
                    ((ObjectNode) gasStation).put("hasTriggerCreated", true);
                } else {
                    ((ObjectNode) gasStation).put("hasTriggerCreated", false);
                }

            }

        }

        // Convert the modified JSON back to a string
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    public GraphQLBody formFindByZipcodeRequestGraphQLBody(String query, String search, int fuel, int maxAge, int brandId, String operationName) {

        GraphQLBody body = new GraphQLBody();
        body.setOperationName(operationName);
        body.setQuery(query);

        GraphQLVars vars = new GraphQLVars();
        vars.setSearch(search);
        vars.setFuel(fuel);
        vars.setMaxAge(maxAge);
        if (brandId != 1) {
            vars.setBrandId(brandId);
        }
        body.setVariables(vars);
        return body;
    }

    public String getBrands() {
        String query = """
                  query Brands {
                    brands {
                      brandId
                      name
                    }
                  }
                """;


        // Serialize the GraphQLRequest object to JSON
        return webClient.post()
                .uri("/graphql")
                .header("Content-Type", "application/json")
                .bodyValue(withoutVar(query, "Brands"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public GraphQLBodyWithoutVar withoutVar(String query, String operationName) {

        GraphQLBodyWithoutVar body = new GraphQLBodyWithoutVar();
        body.setOperationName(operationName);
        body.setQuery(query);
        return body;
    }

    public Map<String, Object> formBrandsPayload(String query) {
        return Map.of(
                "operationName", "Brands",
                "query",          query
        );
    }

    public GraphQLBody formFetchAllBrandsGraphQLBody(String query, String operationName) {

        GraphQLBody body = new GraphQLBody();
        body.setOperationName(operationName);
        body.setQuery(query);

        body.setVariables(new GraphQLVars());
        return body;
    }


}