package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Fuel;
import com.epam.brest.courses.service.FuelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Fuel Service Rest.
 */
public class FuelServiceRest implements FuelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuelServiceRest.class);
    private String url;
    private RestTemplate restTemplate;

    public FuelServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Get all fuels.
     *
     * @return list of all fuels.
     */
    @Override
    public List<Fuel> findAll() {
        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Fuel>) responseEntity.getBody();
    }

    /**
     * Get all fuels wirh specified id.
     *
     * @param fuelId fuel Id.
     * @return fuel by id.
     */
    @Override
    public Optional<Fuel> findById(Integer fuelId) {
        LOGGER.debug("findById({})", fuelId);
        ResponseEntity<Fuel> responseEntity =
                restTemplate.getForEntity(url + "/" + fuelId, Fuel.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    /**
     * Persist new fuel.
     *
     * @param fuel fuel.
     * @return persisted fuel id.
     */
    @Override
    public Integer create(Fuel fuel) {
        LOGGER.debug("create({})", fuel);
        ResponseEntity responseEntity =
                restTemplate.postForEntity(url, fuel ,Integer.class);
        return (Integer) responseEntity.getBody();
    }

    /**
     * update fuel.
     *
     * @param fuel fuel.
     * @return number of updated records in database.
     */
    @Override
    public int update(Fuel fuel) {
        LOGGER.debug("update({})", fuel);
        //ResponseEntity responseEntity = restTemplate.put(url, Fuel.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Fuel> entity = new HttpEntity<>(fuel, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url,
                HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }

    /**
     * delete fuel.
     *
     * @param fuelId fuel Id.
     * @return number of deleted records in database.
     */
    @Override
    public int delete(Integer fuelId) {
        LOGGER.debug("delete({})", fuelId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Fuel> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url + "/" + fuelId,
                HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();
    }
}
