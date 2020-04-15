package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Fuel;
import com.epam.brest.courses.model.Transport;
import com.epam.brest.courses.model.dto.FuelDto;
import com.epam.brest.courses.service.FuelDtoService;
import com.epam.brest.courses.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Transport Service Rest.
 */
public class TransportServiceRest implements TransportService {
    private static Logger LOGGER = LoggerFactory.getLogger(TransportServiceRest.class);
    private String url;
    private RestTemplate restTemplate;

    public TransportServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Get all transports.
     *
     * @return list of all transports.
     */
    @Override
    public List<Transport> findAll() {
        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Transport>) responseEntity.getBody();
    }

    /**
     * Get all transports that has transportDate field value in range from dateFrom to dateTo.
     *
     * @param dateFrom Date range from value.
     * @param dateTo   Date ranhe to value.
     * @return List of all transport in range from to specific dates.
     */
    @Override
    public List<Transport> findAllFromDateToDate(Date dateFrom, Date dateTo) {
        LOGGER.debug("findAllFromDateToDate({}, {})", dateFrom, dateTo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Date> filterMap = new HashMap<>();
        filterMap.put("dateFrom", dateFrom);
        filterMap.put("dateTo", dateTo);
        HttpEntity<Map<String, Date>> request = new HttpEntity<Map<String, Date>>(filterMap, headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, request, List.class);
        return (List<Transport>) responseEntity.getBody();
    }

    /**
     * Get all transports with specified fuel id.
     *
     * @param fuelId fuel id.
     * @return transport by fuel id.
     */
    @Override
    public List<Transport> findByFuelId(Integer fuelId) {
        return null;
    }

    /**
     * Get all transports with specified id.
     *
     * @param transportId transport id.
     * @return transport by id.
     */
    @Override
    public Optional<Transport> findById(Integer transportId) {
        return Optional.empty();
    }

    /**
     * Persist new transport.
     *
     * @param transport transport.
     * @return persisted transport id.
     */
    @Override
    public Integer create(Transport transport) {
        return null;
    }

    /**
     * update transport.
     *
     * @param transport transport.
     * @return number of updated records in database.
     */
    @Override
    public int update(Transport transport) {
        return 0;
    }

    /**
     * Delete transport with specified id.
     *
     * @param transportId transport id.
     * @return number of deleted records in database.
     */
    @Override
    public int delete(Integer transportId) {
        return 0;
    }
}
