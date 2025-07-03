package com.gpn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpn.service.FetchBrandsService;
import com.gpn.service.GraphQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GasStationController {

    private static final Logger logger = LoggerFactory.getLogger(GasStationController.class);

    private final GraphQLService graphQLService;
    private  final FetchBrandsService fetchBrandsService;

    @Autowired
    public GasStationController(GraphQLService graphQLService, FetchBrandsService fetchBrandsService) {
        this.graphQLService = graphQLService;
        this.fetchBrandsService = fetchBrandsService;
    }

    @GetMapping(value = "/findByStationId", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findByStationId(@RequestHeader("stationId") int stationId) {
        logger.info("Called findByStationId with stationId: {}", stationId);
        return graphQLService.findByStationId(stationId);
    }

    @GetMapping(value = "/findByCityOrZipcode", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findByCityOrZipcode(@RequestParam("search") String search, @RequestParam("fuel") int fuel, @RequestParam("maxAge") int maxAge, @RequestParam(name = "brandId", defaultValue = "1") int brandId ) throws JsonProcessingException {
        logger.info("Called findByCityOrZipcode with search: {}, fuel: {}, maxAge: {}, brandId: {}", search, fuel, maxAge, brandId);
        return graphQLService.findByCityOrZipcode(search, fuel, maxAge, brandId);
    }

    @GetMapping(value = "/getBrands", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getBrands() throws JsonProcessingException {
        logger.info("Called getBrands endpoint");
        return fetchBrandsService.getBrands();
    }


}
