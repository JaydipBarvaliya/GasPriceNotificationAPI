package com.gpn.config;

import com.gpn.services.GasStationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GasPriceScheduler {

    private final GasStationService gasStationService;
    private final JavaMailSender mailSender;

    public GasPriceScheduler(GasStationService gasStationService, JavaMailSender mailSender) {
        this.gasStationService = gasStationService;
        this.mailSender = mailSender;
    }

    // Scheduled method to run every 5 minutes
    @Scheduled(fixedRate = 30000) // 100000 ms = 1 minutes
    public void fetchGasPricesAndSendEmail() {
        try {
            // Fetch the gas prices by calling the getStationData method
            String gasPricesData = gasStationService.findByStationId("12910");

            // Send the email with the fetched data
            sendEmail("jaydipbarvaliya55@gmail.com", "Gas Price Update", gasPricesData);

            System.out.println("Gas prices fetched and email sent successfully!");

        } catch (Exception e) {
            System.err.println("Error fetching gas prices or sending email: " + e.getMessage());
        }
    }

    // Method to send an email
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
