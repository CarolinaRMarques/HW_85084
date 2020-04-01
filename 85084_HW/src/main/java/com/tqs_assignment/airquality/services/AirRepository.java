package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.understanding.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirRepository extends JpaRepository<AirQuality,String> {
    AirQuality findByPlace (String place);
}
