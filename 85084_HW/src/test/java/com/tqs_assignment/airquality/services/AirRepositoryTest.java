package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.AirQuality;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AirRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirRepository repo;

    @Test
    public void whenFindByName_thenReturnCoordinates() {
        AirQuality a = new AirQuality("Aveiro,Portugal", "o3", "77", "Good Air Quality");
        entityManager.persistAndFlush(a);

        AirQuality found = repo.findByPlace(a.getPlace());
        assertThat(found.getPlace()).isEqualTo(a.getPlace());
    }

    @Test
    public void whenFindAll_thenReturnAll() {
        AirQuality a = new AirQuality("Aveiro,Portugal", "o3", "77", "Good Air Quality");
        AirQuality b = new AirQuality("Porto,Portugal", "o3", "77", "Good Air Quality");
        List<AirQuality> allAirQuality = Arrays.asList(a, b);

        for (int i = 0; i < allAirQuality.size(); i++) {
            entityManager.persistAndFlush(allAirQuality.get(i));
        }

        List<AirQuality> found = repo.findAll();
        assertThat(found.size()).isEqualTo(allAirQuality.size());
        assertThat(found.get(0)).isEqualTo(allAirQuality.get(0));
        assertThat(found.get(1)).isEqualTo(allAirQuality.get(1));
    }

    @Test
    public void whenFindAllEmpty_thenReturnEmpty() {
        List<AirQuality> fromDb = repo.findAll();
        assertThat(fromDb).isEmpty();
    }

    @Test
    public void whenInvalidName_thenReturnNull() {
        AirQuality fromDb = repo.findByPlace("doesNotExist");
        assertThat(fromDb).isNull();
    }
}