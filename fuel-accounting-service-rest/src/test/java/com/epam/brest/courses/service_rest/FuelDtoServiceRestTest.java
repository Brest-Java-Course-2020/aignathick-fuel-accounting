package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Fuel;
import com.epam.brest.courses.model.dto.FuelDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class FuelDtoServiceRestTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuelDtoServiceRestTest.class);

    public static final String FUELS_URL = "http://localhost:8088/fuels";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    FuelDtoServiceRest fuelDtoService;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        fuelDtoService = new FuelDtoServiceRest(FUELS_URL, restTemplate);
    }

    @Test
    public void shouldFindSumOfFuelByTransport() throws Exception {
        LOGGER.debug("shouldFindSumOfFuelByTransport");

        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FUELS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))
                );

        // when
        List<FuelDto> fuelDtoList = fuelDtoService.findAllWithFuelSum();

        // then
        mockServer.verify();
        assertNotNull(fuelDtoList);
        assertTrue(fuelDtoList.size() > 0);
    }

    /**
     * Create Mock FuelDto stub.
     * @param index int index.
     * @return FuelDto.
     */
    private FuelDto create(int index){
        FuelDto fuelDto = new FuelDto()
                .setFuelId(index)
                .setFuelName("f"+index)
                .setSumFuel(100d + index);
        return fuelDto;
    }
}
