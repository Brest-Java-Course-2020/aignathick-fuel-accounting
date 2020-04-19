package com.epam.brest.courses.web_app.validators;

import com.epam.brest.courses.model.Transport;
import com.epam.brest.courses.util.DateUtilites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.thymeleaf.util.StringUtils;

import static com.epam.brest.courses.constants.TransportConstants.TRANSPORT_NAME_SIZE;
import static com.epam.brest.courses.constants.TransportConstants.TRANSPORT_TANK_CAPASITY_SIZE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransportValidatorTest {
    private Transport transport;
    private TransportNameValidator transportNameValidator;
    private TransportTankCapasityValidator transportTankCapasityValidator;
    private TransportDateValidator transportDateValidator;
    private BindingResult result;

    @BeforeEach
    private  void setup(){
        transport = Mockito.mock(Transport.class);
        result = new BeanPropertyBindingResult(transport, "transport");
        transportNameValidator = new TransportNameValidator();
        transportTankCapasityValidator = new TransportTankCapasityValidator();
        transportDateValidator = new TransportDateValidator();
    }

    @Test
    public void shouldRejectNullTransportName(){
        // given
        Mockito.when(transport.getTransportName()).thenReturn(null);

        // when
        transportNameValidator.validate(transport, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    public void shouldRejectLargeTransportName(){
        // given
        Mockito.when(transport.getTransportName())
                .thenReturn(StringUtils.repeat("*", TRANSPORT_NAME_SIZE+1));

        // when
       transportNameValidator.validate(transport, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    public void shouldValidateTransportName(){
        // given
        Mockito.when(transport.getTransportName())
                .thenReturn(StringUtils.repeat("*", TRANSPORT_NAME_SIZE));

        // when
        transportNameValidator.validate(transport, result);

        // then
        assertFalse(result.hasErrors());
    }

    @Test
    public void shouldRejectTransportTankCapasityNullValue(){
        // given
        Mockito.when(transport.getTransportTankCapasity())
                .thenReturn(null);

        // when
        transportTankCapasityValidator.validate(transport, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    public void shouldRejectTransportTankCapasityLessThanZero(){
        // given
        Mockito.when(transport.getTransportTankCapasity())
                .thenReturn(-1.d);

        // when
        transportTankCapasityValidator.validate(transport, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    public void shouldRejectTransportWithLargeTankCapasity(){
        // given
        Mockito.when(transport.getTransportTankCapasity())
                .thenReturn((double) (TRANSPORT_TANK_CAPASITY_SIZE+1));

        // when
        transportTankCapasityValidator.validate(transport, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    public void shouldAcceptTransporTankCapasity(){
        // given
        Mockito.when(transport.getTransportTankCapasity())
                .thenReturn((double) (TRANSPORT_TANK_CAPASITY_SIZE));

        // when
        transportTankCapasityValidator.validate(transport, result);

        // then
        assertFalse(result.hasErrors());
    }

    @Test
    public void shouldRejectTransportWithEmptyDate(){
        // given
        Mockito.when(transport.getTransportDate())
                .thenReturn(null);

        // when
        transportDateValidator.validate(transport, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    public void shouldAcceptTransportWithDate(){
        // given
        Mockito.when(transport.getTransportDate())
                .thenReturn(DateUtilites.getDateByString("2020-01-01"));

        // when
        transportDateValidator.validate(transport, result);

        // then
        assertFalse(result.hasErrors());
    }
}
