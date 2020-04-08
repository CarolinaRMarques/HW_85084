package com.tqs_assignment.airquality.controllers;

import com.tqs_assignment.airquality.entities.AirQuality;
import com.tqs_assignment.airquality.entities.Coordinates;
import com.tqs_assignment.airquality.services.AirService;
import com.tqs_assignment.airquality.services.CoordinateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc servlet;

    @MockBean
    private AirService airService;

    @MockBean
    private CoordinateService coordService;


    @Test
    void whenGetPlaceQuality_thenReturnAirQuality() throws Exception {
        given(airService.getDataByPlaceName(anyString())).willReturn(
                new AirQuality("Aveiro,Portugal", "o3", "75", "Good air quality"));

        servlet.perform(MockMvcRequestBuilders.get("/airquality/Aveiro,Portugal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("place").value("Aveiro,Portugal"));

        verify(airService, VerificationModeFactory.times(1)).saveAirData(Mockito.any());
        reset(airService);

    }

    @Test
    void whenGetHourlyQuality_thenGetPlaceHistory() throws Exception {
        AirQuality a = new AirQuality("Aveiro,Portugal", "o3", "75", "Good air quality");
        AirQuality b = new AirQuality("Porto,Portugal", "o3", "75", "Good air quality");
        List<AirQuality> allHist = Arrays.asList(a, b);
        given(airService.findData()).willReturn(allHist);

        servlet.perform(MockMvcRequestBuilders.get("/airhistory/Aveiro,Portugal/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].place", is(a.getPlace())))
                .andExpect(jsonPath("$[1].place", is(b.getPlace())));

        verify(airService, VerificationModeFactory.times(1)).saveHistData(Mockito.any());
        reset(airService);
    }

    @Test
    void whenGetCoords_theDisplayCoords() throws Exception {
        given(coordService.getPlaceByCoordinate(anyString())).willReturn(new Coordinates("Aveiro,Portugal", 40.3252, 30.2522));

        servlet.perform(MockMvcRequestBuilders.get("/coords/Aveiro,Portugal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("placename").value("Aveiro,Portugal"));
        verify(coordService, VerificationModeFactory.times(1)).saveCoordinate(Mockito.any());
        reset(coordService);

    }

    @Test
    public void whenGetInexistingPlace_theReturnUndefinedAirQuality() throws Exception {
        String not_valid_city = "not_valid_city";
        given(airService.getDataByPlaceName(not_valid_city)).willReturn(null);
        servlet.perform(MockMvcRequestBuilders.get("/airquality/".concat(not_valid_city)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("place").value("undefined"));

    }

    @Test
    public void whenGetInexistingPlaceCoords_theReturnUndefinedCoordinates() throws Exception {
        String not_valid_city = "not_valid_city";
        given(coordService.getPlaceByCoordinate((not_valid_city))).willReturn(new Coordinates("Non Existing", 0.0, 0.0));
        servlet.perform(MockMvcRequestBuilders.get("/coords/".concat(not_valid_city)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("placename").value("Non Existing"));
    }

    @Test
    public void whenNoPlaceSelected_theReturnUndefinedCoordinates() throws Exception {
        given(coordService.getPlaceByCoordinate(("undefined"))).willReturn(new Coordinates("undefined", 0.0, 0.0));
        servlet.perform(MockMvcRequestBuilders.get("/coords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("placename").value("undefined"));
    }

    @Test
    public void whenGetInexistingPlace_theReturnEmptyList() throws Exception {
        String not_valid_city = "not_valid_city";
        List<AirQuality> allHist = null;
        given(airService.findData()).willReturn(allHist);
        servlet.perform(MockMvcRequestBuilders.get("/airhistory/".concat(not_valid_city) + "/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenGetBadHourRequest_theReturnEmptyList() throws Exception {
        String not_valid_hour = "not_valid_hour";
        List<AirQuality> allHist = null;
        given(airService.findData()).willReturn(allHist);
        servlet.perform(MockMvcRequestBuilders.get("/airhistory/Aveiro,Portugal/".concat(not_valid_hour)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenGetNegativeHourRequest_theReturnEmptyList() throws Exception {
        String not_valid_hour = "-33";
        List<AirQuality> allHist = null;
        given(airService.findData()).willReturn(allHist);
        servlet.perform(MockMvcRequestBuilders.get("/airhistory/Aveiro,Portugal/".concat(not_valid_hour)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenNotGettingAnything_theReturnEmptyList() throws Exception {
        List<AirQuality> allHist = null;
        given(airService.findData()).willReturn(allHist);
        servlet.perform(MockMvcRequestBuilders.get("/airhistory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}