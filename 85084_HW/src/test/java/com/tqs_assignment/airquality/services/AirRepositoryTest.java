package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.AirQuality;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
    public void whenInvalidName_thenReturnNull() {
        AirQuality fromDb = repo.findByPlace("doesNotExist");
        assertThat(fromDb).isNull();
    }
}