package com.epam.brest.courses.web_app.validators;

import com.epam.brest.courses.model.Transport;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.epam.brest.courses.constants.TransportConstants.TRANSPORT_TANK_CAPASITY;
import static com.epam.brest.courses.constants.TransportConstants.TRANSPORT_TANK_CAPASITY_SIZE;

@Component
public class TransportTankCapasityValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Transport.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

       ValidationUtils.rejectIfEmpty(errors, TRANSPORT_TANK_CAPASITY, "transportTankCapasity.empty");
       Transport transport = (Transport) target;
        if (!errors.hasErrors()) {
            if (transport.getTransportTankCapasity() < 0) {
                errors.rejectValue(TRANSPORT_TANK_CAPASITY, "transportTankCapasity.minSize");
            } else if (transport.getTransportTankCapasity() > TRANSPORT_TANK_CAPASITY_SIZE) {
                errors.rejectValue(TRANSPORT_TANK_CAPASITY, "transportTankCapasity.maxSize");
            }
        }
    }
}
