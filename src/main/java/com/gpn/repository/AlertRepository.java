package com.gpn.repository;

import com.gpn.entity.Alerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alerts, Long>{

    @Query("SELECT t.stationId FROM Alerts t")
    List<String> findAllStationIds();

    Alerts findByStationId(int aLong);
}