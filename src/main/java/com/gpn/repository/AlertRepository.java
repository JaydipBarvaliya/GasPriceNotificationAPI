package com.gpn.repository;

import com.gpn.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>{

    @Query("SELECT t.stationId FROM Alert t")
    List<String> findAllStationIds();

    Alert findByStationId(int aLong);
}