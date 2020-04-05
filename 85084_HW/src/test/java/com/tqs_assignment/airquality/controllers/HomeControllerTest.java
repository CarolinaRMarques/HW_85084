package com.tqs_assignment.airquality.controllers;

import com.tqs_assignment.airquality.entities.AirQuality;
import com.tqs_assignment.airquality.entities.Coordinates;
import com.tqs_assignment.airquality.services.AirService;
import com.tqs_assignment.airquality.services.CoordinateService;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class HomeControllerTest {

    @Autowired
    private MockMvc servlet;

    @MockBean
    private AirService airService;

    @MockBean
    private CoordinateService coordService;


    @Test
    void whenGetPlaceQuality_thenReturnAirQuality() throws Exception {
        given(airService.getDataByPlaceName(anyString())).willReturn(new AirQuality("Aveiro,Portugal", "o3", "75", "Good air quality"));

        servlet.perform(MockMvcRequestBuilders.get("/airquality/Aveiro,Portugal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("placename").value("Aveiro,Portugal"));

    }

    @Test
    void getPlaceHistory() {
    }

    @Test
    void whenGetCoords_theDisplayCoords() throws Exception {
        given(coordService.getPlaceByCoordinate(anyString())).willReturn(new Coordinates("Aveiro,Portugal",40.3252,30.2522 ));

        servlet.perform(MockMvcRequestBuilders.get("/coords/Aveiro,Portugal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("placename").value("Aveiro,Portugal"));

    }


    @Test
    public void whenGetInexistingPlace_theReturnNothing() throws Exception {
        String not_valid_city = "not_valid_city";
        //given( carService.getCarDetails(not_valid_car)).willThrow( new CarNotFoundException() ).;
        given( airService.getDataByPlaceName(not_valid_city)).willReturn(null);
        servlet.perform(MockMvcRequestBuilders.get("/airquality/".concat(not_valid_city)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetInexistingPlaceCoords_theReturnNothing() throws Exception {
        String not_valid_city = "not_valid_city";
        given(coordService.getPlaceByCoordinate((not_valid_city))).willReturn(null);
        servlet.perform(MockMvcRequestBuilders.get("/coords/".concat(not_valid_city)))
                .andExpect(status().isNotFound());

    }
}