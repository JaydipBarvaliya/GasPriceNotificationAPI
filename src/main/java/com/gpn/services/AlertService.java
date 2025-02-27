package com.gpn.services;

import com.gpn.entity.Alerts;
import com.gpn.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }


    public Alerts updateAlert(Alerts updatedAlerts) {
        Alerts existingAlerts = alertRepository.findByStationId(updatedAlerts.getStationId());

        // Update fields
        existingAlerts.setExpectedPrice(updatedAlerts.getExpectedPrice());
        existingAlerts.setEmail(updatedAlerts.getEmail());
        existingAlerts.setFuelType(updatedAlerts.getFuelType());
        existingAlerts.setPushNotification(updatedAlerts.getPushNotification());

        // Save updated notifier
        return alertRepository.save(existingAlerts);
    }

    public void save(Alerts alerts) {
        alertRepository.save(alerts);
    }

    public Alerts findByStationId(int stationId) {
        return alertRepository.findByStationId(stationId);
    }

    public List<Alerts> getAlerts() {
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

    public boolean deleteAllAlert() {
        try {
            alertRepository.deleteAll();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
