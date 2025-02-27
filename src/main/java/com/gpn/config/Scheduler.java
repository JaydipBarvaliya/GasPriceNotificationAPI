package com.gpn.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpn.entity.Alerts;
import com.gpn.repository.AlertRepository;
import com.gpn.services.GraphQLService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {

    private final GraphQLService graphQLService;
    private final JavaMailSender mailSender;
    private final AlertRepository alertRepository;

    public Scheduler(GraphQLService graphQLService, JavaMailSender mailSender, AlertRepository alertRepository) {
        this.graphQLService = graphQLService;
        this.mailSender = mailSender;
        this.alertRepository = alertRepository;
    }

//    @Scheduled(fixedRate = 5000) // 100000 ms = 1 minutes
    public void fetchGasPricesAndSendEmail() {
        try {
            List<Alerts> all = alertRepository.findAll();

                for(Alerts alerts : all) {


                    String gasStationLiveData = graphQLService.findByStationId(alerts.getStationId());

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(gasStationLiveData);
                        JsonNode prices = rootNode.at("/data/station/prices");

                        float currentGasPrice = (float)prices.get(alerts.getFuelType()-1).at("/credit/price").asDouble();
                        float userExpectedPrice  = alerts.getExpectedPrice();

                        if(currentGasPrice <= userExpectedPrice) {

                            System.out.println("Gas prices fetched and email sent successfully!");
                        }else{
                            System.out.println("--------------------");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        } catch (Exception e) {
            System.err.println("Error fetching gas prices or sending email: " + e.getMessage());
        }
    }


// sendEmail(alert.getEmail(), "Gas Price Update At" + "", "Current Gas Price: " + currentGasPrice + " Which is lower than your set price " + userExpectedPrice);

// Method to send an email
//    public void sendEmail(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        mailSender.send(message);
//    }
}
