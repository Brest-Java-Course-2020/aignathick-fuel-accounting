package com.epam.brest.courses.dao;

import com.epam.brest.courses.dao.testConfiguration.TestConfig;
import com.epam.brest.courses.model.Fuel;
import com.epam.brest.courses.model.Fuel;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.FuelConstants.FUEL_NAME_SIZE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Fuel.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:dao.properties")
@Sql({"classpath:create-db.sql", "classpath:init-db.sql"})
@ActiveProfiles("test")
public class FuelDaoJdbcIT {

//    private final FuelDao fuelDao;
//
//    @Autowired
//    public FuelDaoJdbcIT(FuelDao fuelDao) {
//        this.fuelDao = fuelDao;
//    }

    @Autowired
    FuelDao fuelDao;

    @Test
    public void shoulFindAllFuels() {
        List<Fuel> fuels = fuelDao.findAll();
        assertNotNull(fuels);
        assertTrue(fuels.size() > 0);
    }

    @Test
    public void shouldFindFuelById(){
        // given
        Fuel fuel = new Fuel()
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));
        Integer id = fuelDao.create(fuel);
        assertNotNull(id);

        // when
        Optional<Fuel> fuelOptional = fuelDao.findById(id);

        // then
        assertTrue(fuelOptional.isPresent());
        assertEquals(fuelOptional.get().getFuelId(), id);
        assertEquals(fuelOptional.get().getFuelName(), fuel.getFuelName());
    }

    @Test
    public void shouldCreateFuel(){
        Fuel fuel = new Fuel()
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));
        Integer id = fuelDao.create(fuel);
        assertNotNull(id);
    }

    @Test
    public void shouldNotCreateFuelWithTheSameName(){
        // given
        String fuelName = RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE);
        Fuel firstNewFuel = new Fuel()
                .setFuelName(fuelName);
        Integer firstId = fuelDao.create(firstNewFuel);
        assertNotNull(firstId);

        Optional<Fuel> firstFuelOptional = fuelDao.findById(firstId);
        assertTrue(firstFuelOptional.isPresent());

        // when
        Fuel secondNewFuel = new Fuel()
                .setFuelName(fuelName);

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> fuelDao.create(secondNewFuel));

    }

    @Test
    public void shouldUpdateFuel(){
        // given
        Fuel fuel = new Fuel()
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));
        Integer id = fuelDao.create(fuel);
        assertNotNull(id);

        Optional<Fuel> fuelOptional = fuelDao.findById(id);
        assertTrue(fuelOptional.isPresent());

        fuelOptional.get()
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));

        // when
        int result = fuelDao.update(fuelOptional.get());

        // then
        assertTrue(1 == result);
        Optional<Fuel> updatedFuelOptional = fuelDao.findById(id);
        assertTrue(updatedFuelOptional.isPresent());
        assertEquals(updatedFuelOptional.get().getFuelId(), id);
        assertEquals(updatedFuelOptional.get().getFuelName(), fuelOptional.get().getFuelName());
    }

    @Test
    public void shouldDeleteFuel(){
        //given
        Fuel fuel = new Fuel()
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));
        Integer id = fuelDao.create(fuel);
        assertNotNull(id);

        List<Fuel> fuels= fuelDao.findAll();
        assertNotNull(fuels);

        // when
        int result = fuelDao.delete(id);

        //then
        assertTrue(1 == result);

        List<Fuel> currentFuels = fuelDao.findAll();
        assertNotNull(currentFuels);

        assertTrue(fuels.size()-1 == currentFuels.size());
    }
}
