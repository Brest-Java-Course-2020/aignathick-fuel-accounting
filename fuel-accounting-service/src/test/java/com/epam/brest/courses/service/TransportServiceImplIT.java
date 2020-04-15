package com.epam.brest.courses.service;

import com.epam.brest.courses.model.Transport;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.TransportConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-service.xml", "classpath*:dao.xml"})
public class TransportServiceImplIT {
    public static final String datePattern = "yyyy-MM-dd";
    public static final String DATE_FROM = "2020-03-01";
    public static final String DATE_FOR_TRANSPORT2 = "2020-03-02";
    public static final String DATE_TO = "2020-03-08";

    @Autowired
    private TransportService transportService;

    @Test
    public void shoulFindAllTransports() {
        List<Transport> transports = transportService.findAll();
        assertNotNull(transports);
        assertTrue(transports.size() > 0);
    }

    private Date getDateByString(String dateAsString) {
        SimpleDateFormat dateformat = new SimpleDateFormat(datePattern);
        try {
            return dateformat.parse(dateAsString);
        } catch (Exception ex) {
            return null;
        }
    }

    @Test
    public void shouldFindAllTransportsInValueFromDateToDate() {
        // given
        Date dateFrom = getDateByString(DATE_FROM);
        Transport transport1 = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(dateFrom);

        Integer id1 = transportService.create(transport1);
        assertNotNull(id1);

        Date dateTransport2 = getDateByString(DATE_FOR_TRANSPORT2);
        Transport transport2 = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(dateTransport2);

        Integer id2 = transportService.create(transport2);
        assertNotNull(id2);
        Date dateTo = getDateByString(DATE_TO);

        // when
        List<Transport> transports = transportService.findAllFromDateToDate(dateFrom, dateTo);
        // then
        assertNotNull(transports);
        assertTrue(2 == transports.size());
    }

    @Test
    public void shouldFindTransportsByFuilId() {
        List<Transport> transports = transportService.findByFuelId(1);
        assertNotNull(transports);
        assertTrue(transports.size() > 0);
        for (Transport transport: transports) {
            assertEquals(Integer.valueOf(1), transport.getFuelId());
        }
    }

    @Test
    public void shouldFindTransportById() {

        // given
        Transport transport = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(getDateWithoutTimeUsingCalendar());

        Integer id = transportService.create(transport);
        assertNotNull(id);

        // when
        Optional<Transport> optionalTransport = transportService.findById(id);

        // then
        assertTrue(optionalTransport.isPresent());
        assertEquals(id, optionalTransport.get().getTransportId());
        assertEquals(transport.getTransportName(), optionalTransport.get().getTransportName());
        assertEquals(transport.getFuelId(), optionalTransport.get().getFuelId());
        assertEquals(transport.getTransportTankCapasity(), optionalTransport.get().getTransportTankCapasity());
        Date dateFromOptional = new Date(optionalTransport.get().getTransportDate().getTime());
        assertEquals(transport.getTransportDate(), dateFromOptional);
    }

    /**
     * Get date without time.
     */
    public static Date getDateWithoutTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    @Test
    public void shouldCreateTransport() {

        Transport transport = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(getDateWithoutTimeUsingCalendar());

        Integer id = transportService.create(transport);
        assertNotNull(id);
    }

    @Test
    public void shouldUpdateTransport() {

        // given
        Transport transport = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(getDateWithoutTimeUsingCalendar());

        Integer id = transportService.create(transport);
        assertNotNull(id);

        Optional<Transport> transportOptional = transportService.findById(id);
        assertTrue(transportOptional.isPresent());
        transportOptional.get()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE));

        //when
        int result = transportService.update(transportOptional.get());

        //then
        assertTrue(1 == result);
        Optional<Transport> updatedTransportOptional = transportService.findById(id);
        assertTrue(updatedTransportOptional.isPresent());
        assertEquals(id, updatedTransportOptional.get().getTransportId());
        assertEquals(transportOptional.get().getTransportName(), updatedTransportOptional.get().getTransportName());
    }

    @Test
    public void shouldDeleteTransport() {

        // given
        Transport transport = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(getDateWithoutTimeUsingCalendar());

        Integer id = transportService.create(transport);
        assertNotNull(id);

        List<Transport> transports =
                transportService.findAll();
        assertNotNull(transports);

        // when
        int result = transportService.delete(id);
        assertTrue(1 == result);

        List<Transport> currentTransports = transportService.findAll();
        assertNotNull(currentTransports);
        assertEquals(transports.size() - 1, currentTransports.size());
    }

}
