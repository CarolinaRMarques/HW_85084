package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoordinateService {
    @Autowired
    CoordinateRepository coordinateRepository;

    public CoordinateService(CoordinateRepository coordinateRepository){this.coordinateRepository = coordinateRepository; }

    public Coordinates getPlaceByCoordinate(String placename){
        return coordinateRepository.findByPlacename(placename);
    }

    public void saveCoordinate (Coordinates coordinate){
        coordinateRepository.save(coordinate);
    }
}
