package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Fuel;
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
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.FuelConstants.FUEL_NAME_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class FuelServiceRestTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuelServiceRestTest.class);
    public static final String FUELS_URL = "http://localhost:8088/fuels";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    FuelServiceRest fuelService;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        fuelService = new FuelServiceRest(FUELS_URL, restTemplate);
    }

    @Test
    public void shouldFindAllFuel() throws Exception {

        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FUELS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))
                );
        // when
        List<Fuel> fuelList = fuelService.findAll();

        // then
        mockServer.verify();
        assertNotNull(fuelList);
        assertTrue(fuelList.size() > 0);
    }

    @Test
    public void shouldCreateFuel() throws Exception {

        // given
        Fuel fuel = new Fuel()
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FUELS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString("1"))
                );

        // when
        Integer id = fuelService.create(fuel);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

    @Test
    public void shouldFindFuelById() throws Exception {

        // given
        Integer id = 1;
        Fuel fuel = new Fuel()
                .setFuelId(id)
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FUELS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(fuel))
                );
        // when
        Optional<Fuel> OptionalFuel = fuelService.findById(id);

        // then
        mockServer.verify();
        assertTrue(OptionalFuel.isPresent());
        assertEquals(OptionalFuel.get().getFuelId(), id);
        assertEquals(OptionalFuel.get().getFuelName(), fuel.getFuelName());
    }

    @Test
    public void shouldUpdateFuel() throws Exception {

        // given
        Integer id = 1;
        Fuel fuel = new Fuel()
                .setFuelId(id)
                .setFuelName(RandomStringUtils.randomAlphabetic(FUEL_NAME_SIZE));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FUELS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FUELS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(fuel))
                );

        //when
        int result = fuelService.update(fuel);
        Optional<Fuel> updatedOptionalFuel = fuelService.findById(id);

        //then
        mockServer.verify();
        assertTrue(1 == result);
        assertTrue(updatedOptionalFuel.isPresent());
        assertEquals(updatedOptionalFuel.get().getFuelId(), id);
        assertEquals(updatedOptionalFuel.get().getFuelName(), fuel.getFuelName());
    }

    @Test
    public void shouldDeleteFuel() throws Exception {

        // given
        Integer id = 1;

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FUELS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        int result = fuelService.delete(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);
    }

    /**
     * Create Mock Fuel stub.
     * @param index int index.
     * @return Fue.
     */
    private Fuel create(int index){
        Fuel fuel= new Fuel()
                .setFuelId(index)
                .setFuelName("f"+index);
        return fuel;
    }
}
