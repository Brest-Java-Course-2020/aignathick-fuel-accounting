package com.epam.brest.courses.dao;

import com.epam.brest.courses.model.dto.FuelDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fuel DTO Service Interface Implementation.
 */
@Component
@PropertySource("classpath:dao.properties")
public class FuelDtoDaoJdbc implements FuelDtoDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static Logger LOGGER = LoggerFactory.getLogger(FuelDtoDaoJdbc.class);

    @Value("${fuelDto.findAllWithFuelTankCapasitySum}")
    private String findAllWithFuelTankCapasitySum;

    public FuelDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Get sum of fuel by transport.
     *
     * @return fuel list.
     */
    @Override
    public List<FuelDto> findAllWithFuelSum() {
        LOGGER.debug("findAllWithFuelSum()");
        return namedParameterJdbcTemplate.query(findAllWithFuelTankCapasitySum,
                BeanPropertyRowMapper.newInstance(FuelDto.class));
    }
}
