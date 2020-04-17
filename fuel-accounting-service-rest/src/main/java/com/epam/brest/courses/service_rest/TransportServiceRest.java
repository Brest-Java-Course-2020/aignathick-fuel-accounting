package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Transport;
import com.epam.brest.courses.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Transport Service Rest.
 */
public class TransportServiceRest implements TransportService {

    public static final String SEARCH_BY_FUEL_ID_URL = "/fuel/";
    public static final String FILTER_URL = "/filter";
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
        //ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Transport> entity = new HttpEntity<>(null, headers);
        ParameterizedTypeReference<List<Transport>> responseType = new ParameterizedTypeReference<List<Transport>>() {};
        ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
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
        //ResponseEntity responseEntity = restTemplate.postForEntity(url + FILTER_URL, request, List.class);
        ParameterizedTypeReference<List<Transport>> responseType = new ParameterizedTypeReference<List<Transport>>() {};
        ResponseEntity responseEntity = restTemplate.exchange(url + FILTER_URL, HttpMethod.POST, request, responseType);
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
        LOGGER.debug("findByFuelId({})", fuelId);
        ResponseEntity responseEntity = restTemplate.getForEntity(url+ SEARCH_BY_FUEL_ID_URL + fuelId, List.class);
        return (List<Transport>) responseEntity.getBody();
    }

    /**
     * Get all transports with specified id.
     *
     * @param transportId transport id.
     * @return transport by id.
     */
    @Override
    public Optional<Transport> findById(Integer transportId) {
        ResponseEntity<Transport> responseEntity =
                restTemplate.getForEntity(url + "/" + transportId, Transport.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    /**
     * Persist new transport.
     *
     * @param transport transport.
     * @return persisted transport id.
     */
    @Override
    public Integer create(Transport transport) {
        LOGGER.debug("create({})", transport);
        ResponseEntity responseEntity =
                restTemplate.postForEntity(url, transport ,Integer.class);
        return (Integer) responseEntity.getBody();
    }

    /**
     * update transport.
     *
     * @param transport transport.
     * @return number of updated records in database.
     */
    @Override
    public int update(Transport transport) {
        LOGGER.debug("update({})", transport);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Transport> entity = new HttpEntity<>(transport, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url,
                HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }

    /**
     * Delete transport with specified id.
     *
     * @param transportId transport id.
     * @return number of deleted records in database.
     */
    @Override
    public int delete(Integer transportId) {

        LOGGER.debug("delete({})", transportId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Transport> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url + "/" + transportId,
                HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();
    }
}
