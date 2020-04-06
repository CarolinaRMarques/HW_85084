package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CoordinateServiceTest {

    @Mock(lenient = true)
    private CoordinateRepository coordRepository;
    @InjectMocks
    private CoordinateService coordService;

    @BeforeEach
    public void setUp() throws Exception {
        Coordinates a = new Coordinates("Aveiro,Portugal", 30.0215, 40.2520);

        Mockito.when(coordRepository.findByPlacename(a.getPlacename())).thenReturn(a);
        Mockito.when(coordRepository.findByPlacename("wrong_name")).thenReturn(null);
    }

    @Test
    void whenValidPlace_getPlaceByCoordinate() {
        String name = "Aveiro,Portugal";
        Coordinates found = coordRepository.findByPlacename(name);
        assertThat(found.getPlacename()).isEqualTo(name);
    }

    @Test
    public void whenValidPlace_thenCoordinatesyDetailsAreCorrect() {
        Double longitude = 40.2520;
        Double latitude = 30.0215;
        assertThat(coordService.getPlaceByCoordinate("Aveiro,Portugal").getLatitude()).isEqualTo(latitude);
        assertThat(coordService.getPlaceByCoordinate("Aveiro,Portugal").getLongitude()).isEqualTo(longitude);
    }

    @Test
    public void whenInvalidPlaceName_thenReturnNull() {
        Coordinates fromDb = coordRepository.findByPlacename("doesNotExist");
        assertThat(fromDb).isNull();
    }


}