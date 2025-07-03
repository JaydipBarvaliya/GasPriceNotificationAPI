package com.gpn.service;

import com.gpn.dto.GraphQLBodyWithoutVar;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FetchBrandsService {

    private final WebClient webClient;

    public FetchBrandsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://www.gasbuddy.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ORIGIN,  "https://www.gasbuddy.com")
                .defaultHeader(HttpHeaders.REFERER, "https://www.gasbuddy.com/")
                .defaultHeader(HttpHeaders.USER_AGENT,
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                "Chrome/115.0.0.0 Safari/537.36")
                // ← paste your full cookie string here, e.g. "__cf_bm=…; _loc_iu=…; _gcl_au=…; …"
                .defaultHeader(HttpHeaders.COOKIE,
                        "__cf_bm=SIGQprJd83mzNffa6v.NnJTSu44AMfz…; " +
                                "_loc_iu=\"e26d2dfb-0e39-…\"; _loc_ids={}; _gcl_au=1.1.1699795465.1751304402; …")
                // ← and your CSRF header if present
                .defaultHeader("gbcsrf","1.XXgxlk/f1kVCXycp")
                .build();
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

        // Now sends exactly { operationName, query } + the headers above
        return webClient.post()
                .uri("/graphql")
                .bodyValue(withoutVar(query, "Brands"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private GraphQLBodyWithoutVar withoutVar(String query, String operationName) {
        GraphQLBodyWithoutVar b = new GraphQLBodyWithoutVar();
        b.setOperationName(operationName);
        b.setQuery(query);
        return b;
    }
}
