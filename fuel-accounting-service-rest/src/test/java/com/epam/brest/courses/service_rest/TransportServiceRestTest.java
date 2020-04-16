package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Fuel;
import com.epam.brest.courses.model.Transport;
import com.epam.brest.courses.model.dto.FuelDto;
import com.epam.brest.courses.service_rest.util.DateUtilites;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.FuelConstants.FUEL_NAME_SIZE;
import static com.epam.brest.courses.constants.TransportConstants.TRANSPORT_NAME_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class TransportServiceRestTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportServiceRestTest.class);
    public static final String SEARCH_BY_FUEL_ID_URL = "/fuel/";
    public static final String FILTER_URL = "/filter";
    public static final String TRANSPORTS_URL = "http://localhost:8088/transports";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    TransportServiceRest transportsService;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        transportsService = new TransportServiceRest(TRANSPORTS_URL, restTemplate);
    }

    @Test
    public void shouldFindAllFuel() throws Exception {

        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRANSPORTS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                create(0, DateUtilites.getDateByString("2020-01-01")),
                                create(1, DateUtilites.getDateByString("2020-01-02")))))
                );
        // when
        List<Transport> transportList = transportsService.findAll();

        // then
        mockServer.verify();
        assertNotNull(transportList);
        assertTrue(transportList.size() > 0);
    }

    @Test
    public void shouldFindTransportInDatesRange() throws Exception {
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRANSPORTS_URL + FILTER_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                create(0, DateUtilites.getDateByString("2020-01-01")),
                                create(1, DateUtilites.getDateByString("2020-01-02"))
                        )))
                );
        // when
        List<Transport> transportList = transportsService.findAllFromDateToDate(
                DateUtilites.getDateByString("2020-01-01"),
                DateUtilites.getDateByString("2020-01-02")
        );

        // then
        mockServer.verify();
        assertNotNull(transportList);
        assertTrue(2 ==transportList.size());
    }

    @Test
    public void shouldFindTransportByFuelId() throws Exception {

        // given
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(TRANSPORTS_URL + SEARCH_BY_FUEL_ID_URL + "1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                create(0, DateUtilites.getDateByString("2020-01-01")),
                                create(1, DateUtilites.getDateByString("2020-01-02")))))
                );
        // when
        List<Transport> transportList = transportsService.findByFuelId(1);

        // then
        mockServer.verify();
        assertNotNull(transportList);
        assertTrue(transportList.size() > 0);

    }

    @Test
    public void shouldFindTransportById() throws Exception {
        // given
        Integer id = 1;
        Transport transport = new Transport()
                .setTransportId(id)
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setTransportTankCapasity(50d)
                .setTransportDate(DateUtilites.getMonthStartDate())
                .setFuelId(1);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRANSPORTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(transport))
                );
        // when
        Optional<Transport> OptionalTransport = transportsService.findById(id);

        // then
        mockServer.verify();
        assertTrue(OptionalTransport.isPresent());
        assertEquals(OptionalTransport.get().getTransportId(), id);
        assertEquals(OptionalTransport.get().getTransportName(), transport.getTransportName());
        assertEquals(OptionalTransport.get().getTransportDate(), transport.getTransportDate());
        assertEquals(OptionalTransport.get().getFuelId(), transport.getFuelId());
        assertEquals(OptionalTransport.get().getTransportTankCapasity(), transport.getTransportTankCapasity());
    }

    @Test
    public void shouldCreateTransport() throws Exception {

        // given
        Transport transport = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRANSPORTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        Integer id = transportsService.create(transport);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

    @Test
    public void shouldUpdateTransport() throws Exception {

        // given
        Integer id = 1;
        Transport transport = new Transport()
                .setFuelId(id)
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setTransportTankCapasity(50d)
                .setTransportDate(DateUtilites.getMonthStartDate())
                .setFuelId(1);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRANSPORTS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRANSPORTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(transport))
                );

        //when
        int result = transportsService.update(transport);
        Optional<Transport> updatedOptionalTransport = transportsService.findById(id);

        //then
        mockServer.verify();
        assertTrue(1 == result);
        assertTrue(updatedOptionalTransport.isPresent());
        assertEquals(updatedOptionalTransport.get().getFuelId(), id);
        assertEquals(updatedOptionalTransport.get().getTransportName(), transport.getTransportName());
        assertEquals(updatedOptionalTransport.get().getTransportDate(), transport.getTransportDate());
        assertEquals(updatedOptionalTransport.get().getFuelId(), transport.getFuelId());
        assertEquals(updatedOptionalTransport.get().getTransportTankCapasity(), transport.getTransportTankCapasity());
    }

    @Test
    public void shouldDeleteTransport() throws Exception {

        // given
        Integer id = 1;

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRANSPORTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        int result = transportsService.delete(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);
    }

    /**
     * Create Mock Transport stub.
     * @param index int index.
     * @param date Date.
     * @return Transport.
     */
    private Transport create(int index, Date date){
        Transport transport = new Transport()
                .setTransportId(index)
                .setTransportName("t"+index)
                .setTransportTankCapasity(50d + index)
                .setTransportDate(date)
                .setFuelId(1);
        return transport;
    }
}
