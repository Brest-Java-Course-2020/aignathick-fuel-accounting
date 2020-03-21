package com.epam.brest.courses.web_app;

import com.epam.brest.courses.model.Fuel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.epam.brest.courses.constants.FuelConstants.*;
import static com.epam.brest.courses.constants.TransportConstants.*;
import static com.epam.brest.courses.constants.TransportConstants.TRANSPORT_TANK_CAPASITY;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:app-context-test.xml"})
@Transactional
public class TransportControllerIT {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Test
    public void shouldReturnTransportsPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/transports")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("transports"));
    }

    @Test
    public void shouldFindTransportInDateRange() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/transports/from/2020-01-01/to/2020-01-03")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("transports"))
                .andExpect(MockMvcResultMatchers.model().attribute("transports", org.hamcrest.Matchers.hasItem(
                        Matchers.allOf(
                                Matchers.hasProperty(TRANSPORT_ID, Matchers.is(1)),
                                Matchers.hasProperty(TRANSPORT_NAME, Matchers.is("Renault Megane")),
                                Matchers.hasProperty(TRANSPORT_FUEL_ID, Matchers.is(1)),
                                //Matchers.hasProperty(TRANSPORT_DATE, Matchers.is(getDateByString("01/01/2020"))),
                                Matchers.hasProperty(TRANSPORT_DATE, Matchers.instanceOf (Date.class)),
                                Matchers.hasProperty(TRANSPORT_TANK_CAPASITY, Matchers.is(45.d))))));
    }

    private Date getDateByString(String dateAsString) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateformat.parse(dateAsString);
        } catch (Exception ex) {
            return null;
        }
    }

    @Test
    public void shoudOpenEditTransportPageById() throws Exception {
        mockMvc.perform(
         MockMvcRequestBuilders.get("/transport/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transport"))
                .andExpect(model().attribute("isNew", false))
                .andExpect(model().attribute("transport", hasProperty(TRANSPORT_ID, is(1))))
                .andExpect(model().attribute("transport", hasProperty(TRANSPORT_NAME, is("Renault Megane"))))
                .andExpect(model().attribute("transport", hasProperty(TRANSPORT_FUEL_ID, is(1))))
                .andExpect(model().attribute("transport", hasProperty(TRANSPORT_DATE, instanceOf(Date.class))))
                //.andExpect(model().attribute("transport", hasProperty(TRANSPORT_DATE, is(getDateByString("01/01/2020")))))
                .andExpect(model().attribute("transport", hasProperty(TRANSPORT_TANK_CAPASITY, is(45.d))));

    }

}
