package com.tqs_assignment.airquality.services;

import com.tqs_assignment.airquality.entities.Coordinates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CoordinateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CoordinateRepository repo;

    @Test
    public void whenFindByName_thenReturnCoordinates() {
        Coordinates a = new Coordinates("Aveiro,Portugal", 30.0215,40.2520);
        entityManager.persistAndFlush(a);

        Coordinates found = repo.findByPlacename(a.getPlacename());
        assertThat(found.getPlacename()).isEqualTo(a.getPlacename());
    }

    @Test
    public void whenInvalidName_thenReturnNull() {
        Coordinates fromDb = repo.findByPlacename("doesNotExist");
        assertThat(fromDb).isNull();
    }
}