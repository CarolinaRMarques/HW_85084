package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinates, String> {
    Coordinates findByPlacename(String place);
}
