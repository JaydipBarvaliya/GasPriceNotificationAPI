package com.gpn.services;

import com.gpn.entity.Alert;
import com.gpn.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }


    public Alert updateAlert(Alert updatedAlert) {
        Alert existingAlert = alertRepository.findByStationId(updatedAlert.getStationId());

        // Update fields
        existingAlert.setExpectedPrice(updatedAlert.getExpectedPrice());
        existingAlert.setEmail(updatedAlert.getEmail());
        existingAlert.setFuelType(updatedAlert.getFuelType());
        existingAlert.setPushNotification(updatedAlert.getPushNotification());

        // Save updated notifier
        return alertRepository.save(existingAlert);
    }

    public void save(Alert alert) {
        alertRepository.save(alert);
    }

    public Alert findByStationId(int stationId) {
        return alertRepository.findByStationId(stationId);
    }

    public List<Alert> getAlerts() {
        return alertRepository.findAll();
    }

    public boolean deleteAlertById(Long id) {
        try {
            alertRepository.deleteById(id);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
