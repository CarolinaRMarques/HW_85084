package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.AirQuality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AirServiceTest {
    @Mock(lenient = true)
    private AirRepository airRepository;
    @InjectMocks
    private AirService airService;

    @BeforeEach
    public void setUp() throws Exception {
        AirQuality a = new AirQuality("Aveiro,Portugal", "o3", "77", "Good Air Quality");
        AirQuality b = new AirQuality("Porto,Portugal", "o3", "77", "Good Air Quality");

        List<AirQuality> allAirQuality = Arrays.asList(a,b);
        Mockito.when(airRepository.findAll()).thenReturn(allAirQuality);
        Mockito.when(airRepository.findByPlace(a.getPlace())).thenReturn(a);
        Mockito.when(airRepository.findByPlace("wrong_name")).thenReturn(null);
    }

    @Test
    void whenValidPlace_findByPlace() {
        String name =  "Aveiro,Portugal";
        AirQuality found = airRepository.findByPlace(name);
        assertThat(found.getPlace()).isEqualTo(name);
    }

    @Test
    public void whenInvalidPlaceName_thenReturnNull() {
        AirQuality fromDb = airRepository.findByPlace("doesNotExist");
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenValidPlace_thenAirQualityDetailsAreCorrect() {
        String pollutant = "o3";
        assertThat(airService.getDataByPlaceName("Aveiro,Portugal").getDomminant_pollutant()).isEqualTo(pollutant);
    }

    @Test
    public void given2AirQualities_whengetAll_thenReturn3Records() {
        AirQuality a = new AirQuality("Aveiro,Portugal", "o3", "77", "Good Air Quality");
        AirQuality b = new AirQuality("Porto,Portugal", "o3", "77", "Good Air Quality");
        List<AirQuality> allAirQuality = airService.findData();
        verifyFindAllAirQualityIsCalledOnce();
        assertThat(allAirQuality).hasSize(2).extracting(AirQuality::getPlace).contains(a.getPlace(),b.getPlace());
    }


    private void verifyFindAllAirQualityIsCalledOnce() {
        Mockito.verify(airRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(airRepository);
    }
}