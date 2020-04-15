package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.dto.FuelDto;
import com.epam.brest.courses.service.FuelDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Fuel Dtos Service Rest.
 */
public class FuelDtoServiceRest implements FuelDtoService {
    private static Logger LOGGER = LoggerFactory.getLogger(FuelDtoServiceRest.class);
    private String url;
    private RestTemplate restTemplate;

    public FuelDtoServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Find all fuels with sum of transports tank capasity by fuel type.
     *
     * @return Fuels DTO list,
     */
    @Override
    public List<FuelDto> findAllWithFuelSum() {
        LOGGER.debug("findAllWithFuelSum()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<FuelDto>) responseEntity.getBody();
    }
}
