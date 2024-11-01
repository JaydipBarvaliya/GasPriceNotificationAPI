package com.gpn.services;

import com.gpn.util.GasUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GasStationService {

    private final WebClient webClient;

    public GasStationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.gasbuddy.com").build();
    }

    public String findByStationId(String stationId) {
        String query = "query GetStation($id: ID!) { station(id: $id) { prices { credit { nickname postedTime price } } } }";

        String block = webClient.post()
                .uri("/graphql")
                .header("Content-Type", "application/json")
                .bodyValue(GasUtil.createRequestBody(stationId, query)) // Serialize the GraphQLRequest object to JSON
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return block; // Use block() for synchronous call, otherwise use Mono for async handling
    }



}
