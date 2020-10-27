package com.epam.brest.courses.dao.testConfiguration;

import com.epam.brest.courses.dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
@Configuration
public class TestConfig {

    @Bean
    public FuelDaoJdbc FuelDao() {
        return new FuelDaoJdbc(namedParameterJdbcTemplate());
    }

//    @Bean
//    public FuelDtoDaoJdbc FuelDtoDao(){
//        return new FuelDtoDaoJdbc(namedParameterJdbcTemplate());
//    }
//
//    @Bean
//    public TransportDaoJdbc TransportDao(){
//        return new TransportDaoJdbc(namedParameterJdbcTemplate());
//    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    @Profile("test")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return  dataSource;
    }

}
