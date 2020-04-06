package com.tqs_assignment.airquality;

import com.tqs_assignment.airquality.entities.AirQuality;
import com.tqs_assignment.airquality.entities.Coordinates;
import com.tqs_assignment.airquality.services.AirRepository;
import com.tqs_assignment.airquality.services.CoordinateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AirqualityApplicationTests {

    @Autowired
    private AirRepository airRepository;

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private TestRestTemplate restClient;

    @BeforeEach
    public void setUpCoordsAndAirQuality() throws Exception {
        airRepository.saveAndFlush(new AirQuality("Aveiro,Portugal", "o3", "77", "Good Air Quality"));
        coordinateRepository.saveAndFlush(new Coordinates("Porto,Portugal", 40.3054, 3.36524));

    }

    @AfterEach
    public void resetDb() {
        airRepository.deleteAll();
        coordinateRepository.deleteAll();
    }

    @Test
    void getAirQuality_returnAirQualityDetailsTest() {
        ResponseEntity<AirQuality> entity = restClient.getForEntity("/airquality/Aveiro,Portugal", AirQuality.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getPlace()).isEqualTo("Aveiro,Portugal");
    }


    @Test
    void getCoordinates_returnCoordinatesDetailsTest() {
        ResponseEntity<Coordinates> entity = restClient.getForEntity("/coords/Porto,Portugal", Coordinates.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getPlacename()).isEqualTo("Porto,Portugal");
    }

}