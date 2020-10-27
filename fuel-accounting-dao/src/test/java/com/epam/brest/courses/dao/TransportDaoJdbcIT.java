package com.epam.brest.courses.dao;

import com.epam.brest.courses.model.Transport;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.TransportConstants.TRANSPORT_NAME_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransportDaoJdbcIT {

    public static final String DATE_FROM = "2020-03-01";
    public static final String DATE_FOR_TRANSPORT2 = "2020-03-02";
    public static final String DATE_TO = "2020-03-08";
    public static final String PATTERN = "yyyy-MM-dd";
    private final TransportDao transportDao;

    @Autowired
    public TransportDaoJdbcIT(TransportDao transportDao) {
        this.transportDao = transportDao;
    }

    @Test
    public void shoulFindAllTransports() {
        List<Transport> transports = transportDao.findAll();
        assertNotNull(transports);
        assertTrue(transports.size() > 0);
    }

    private Date getDateByString(String dateAsString) {
        SimpleDateFormat dateformat = new SimpleDateFormat(PATTERN);
        try {
            return dateformat.parse(dateAsString);
        } catch (Exception ex) {
            return null;
        }
    }

    @Test
    public void shouldFindAllTransportsInValueFromToDate() {
        // given
        Date dateFrom = getDateByString(DATE_FROM);
        Transport transport1 = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(dateFrom);

        Integer id1 = transportDao.create(transport1);
        assertNotNull(id1);

        Date dateTransport2 = getDateByString(DATE_FOR_TRANSPORT2);
        Transport transport2 = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(dateTransport2);

        Integer id2 = transportDao.create(transport2);
        assertNotNull(id2);
        Date dateTo = getDateByString(DATE_TO);

        // when
        List<Transport> transports = transportDao.findAllFromDateToDate(dateFrom, dateTo);
        // then
        assertNotNull(transports);
        assertTrue(2 == transports.size());
    }

    @Test
    public void shouldFindTransportsByFuilId() {
        List<Transport> transports = transportDao.findByFuelId(1);
        assertNotNull(transports);
        assertTrue(transports.size() > 0);
    }

    @Test
    public void shouldFindTransportById() {

        // given
        Transport transport = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(getDateWithoutTimeUsingCalendar());

        Integer id = transportDao.create(transport);
        assertNotNull(id);

        // when
        Optional<Transport> optionalTransport = transportDao.findById(id);

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

        Integer id = transportDao.create(transport);
        assertNotNull(id);
    }

    @Test
    public void shouldNotCreateTransportWithTheSameName(){
        // given
        String transportName = RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE);
        Transport transport1 = new Transport()
                .setTransportName(transportName)
                .setFuelId(1)
                .setTransportTankCapasity(40d)
                .setTransportDate(new Date());
        Integer firstId = transportDao.create(transport1);
        assertNotNull(firstId);

        Optional<Transport> firstTransportOptional = transportDao.findById(firstId);
        assertTrue(firstTransportOptional.isPresent());

        // when
        Transport transport2 = new Transport()
                .setTransportName(transportName)
                .setFuelId(2)
                .setTransportTankCapasity(40d)
                .setTransportDate(new Date());

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> transportDao.create(transport2));

    }

    @Test
    public void shouldUpdateTransport() {

        // given
        Transport transport = new Transport()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE))
                .setFuelId(1)
                .setTransportTankCapasity(50d)
                .setTransportDate(getDateWithoutTimeUsingCalendar());

        Integer id = transportDao.create(transport);
        assertNotNull(id);

        Optional<Transport> transportOptional = transportDao.findById(id);
        assertTrue(transportOptional.isPresent());
        transportOptional.get()
                .setTransportName(RandomStringUtils.randomAlphabetic(TRANSPORT_NAME_SIZE));

        //when
        int result = transportDao.update(transportOptional.get());

        //then
        assertTrue(1 == result);
        Optional<Transport> updatedTransportOptional = transportDao.findById(id);
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

        Integer id = transportDao.create(transport);
        assertNotNull(id);

        List<Transport> transports =
                transportDao.findAll();
        assertNotNull(transports);

        // when
        int result = transportDao.delete(id);
        assertTrue(1 == result);

        List<Transport> currentTransports = transportDao.findAll();
        assertNotNull(currentTransports);
        assertEquals(transports.size() - 1, currentTransports.size());
    }
}
