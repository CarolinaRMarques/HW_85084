package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.AirQuality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirService {
    @Autowired
    AirRepository airRepository;

    public AirService(AirRepository airRepository) {
        this.airRepository = airRepository;
    }

    public AirQuality getDataByPlaceName(String placename) {
        return airRepository.findByPlace(placename);
    }

    public void saveAirData(AirQuality air) {
        airRepository.save(air);
    }

    public void saveHistData(ArrayList<AirQuality> air) {
        airRepository.deleteAll();
        for (int i = 0; i < air.size(); i++) {
            airRepository.save(air.get(i));
        }
    }

    public List<AirQuality> findData() {
        return airRepository.findAll();
    }

}
