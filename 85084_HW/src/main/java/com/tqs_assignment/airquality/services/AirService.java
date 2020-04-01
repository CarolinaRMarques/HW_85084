package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.AirQuality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirService {
    @Autowired
    AirRepository airRepository;

    public AirService(AirRepository airRepository){this.airRepository = airRepository; }

    public AirQuality getDataByPlaceName(String placename){
        return airRepository.findByPlace(placename);
    }

    public void saveAirData (AirQuality air){
       airRepository.save(air);
    }
}
