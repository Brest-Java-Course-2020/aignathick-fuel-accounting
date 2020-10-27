package com.epam.brest.courses.dao;

import com.epam.brest.courses.model.dto.FuelDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class FuelDtoDaoJdbcIT {
    @Autowired
    FuelDtoDao fuelDtoDao;

    @Test
    public void shouldFindSumOfFuelByTransport(){
        List<FuelDto> fuels = fuelDtoDao.findAllWithFuelSum();
        assertNotNull(fuels);
        assertTrue(fuels.size()>0);
        if (fuels.size()>0) {
           FuelDto firstFoundedFuel = fuels.get(0);
           assertNotNull(firstFoundedFuel.getSumFuel());
           assertTrue(firstFoundedFuel.getSumFuel()>0);
        }
    }
}
