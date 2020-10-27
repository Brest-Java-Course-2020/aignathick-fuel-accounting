package com.epam.brest.courses.dao;


import com.epam.brest.courses.model.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.brest.courses.constants.TransportConstants.*;
/**
 * TRANSPORT DAO JDBC implementation.
 */
@Component
@PropertySource("classpath:dao.properties")
public class TransportDaoJdbc implements TransportDao {

    private final Logger LOGGER = LoggerFactory.getLogger(TransportDaoJdbc.class);
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Value("${transport.findAll}")
    private String findAll;
    @Value("${transport.findAllFromDateToDate}")
    private String findAllFromDateToDate;
    @Value("${transport.findAllByFuelId}")
    private String findAllByFuelId;
    @Value("${transport.findById}")
    private String findById;
    @Value("${transport.create}")
    private String createSql;
    @Value("${transport.update}")
    private String updateSql;
    @Value("${transport.delete}")
    private String deleteSql;
    @Value("${transport.findCountByName}")
    private String findCountByNameSql;

    public TransportDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Transport> findAll() {
        LOGGER.debug("findAll()");
        return namedParameterJdbcTemplate.query(findAll, BeanPropertyRowMapper.newInstance(Transport.class));
    }

    @Override
    public List<Transport> findAllFromDateToDate(Date dateFrom, Date dateTo) {
        LOGGER.debug("findAllFromDateToDate(dateFrom:{}, dateTo:{})", dateFrom, dateTo);
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(DATE_FROM, dateFrom, Types.DATE);
        namedParameters.addValue(DATE_TO, dateTo, Types.DATE);
        return namedParameterJdbcTemplate.query(findAllFromDateToDate,
                namedParameters, BeanPropertyRowMapper.newInstance(Transport.class));

    }

    @Override
    public List<Transport> findByFuelId(Integer fuelId) {
        LOGGER.debug("findByFuelId(id:{})", fuelId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(TRANSPORT_FUEL_ID, fuelId);
        return namedParameterJdbcTemplate.query(findAllByFuelId,
                sqlParameterSource, BeanPropertyRowMapper.newInstance(Transport.class));
    }

    @Override
    public Optional<Transport> findById(Integer transportId) {
        LOGGER.debug("findById(id:{})", transportId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(TRANSPORT_ID, transportId);
        List<Transport> transports =
                namedParameterJdbcTemplate.query(findById, sqlParameterSource, BeanPropertyRowMapper.newInstance(Transport.class));
        return Optional.ofNullable(DataAccessUtils.uniqueResult(transports));
    }

    @Override
    public Integer create(Transport transport) {

        LOGGER.debug("create(transport:{})", transport);
        if (!isNameUnique(transport)) {
            throw new IllegalArgumentException("transport with same name already exits in DB.");
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(TRANSPORT_NAME, transport.getTransportName());
        parameters.addValue(TRANSPORT_FUEL_ID, transport.getFuelId());
        parameters.addValue(TRANSPORT_TANK_CAPASITY, transport.getTransportTankCapasity());
        parameters.addValue(TRANSPORT_DATE, transport.getTransportDate());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(createSql, parameters, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    /**
     * Find count of transport name in table transports.
     * @param transport Transport.
     * @return numbers of rows find in table transports.
     */
    @SuppressWarnings("ConstantConditions")
    private boolean isNameUnique(Transport transport) {
        LOGGER.debug("isNameUnique({})", transport);
        return namedParameterJdbcTemplate.queryForObject(findCountByNameSql,
                new MapSqlParameterSource(TRANSPORT_NAME, transport.getTransportName()),
                Integer.class) == 0;
    }

    @Override
    public int update(Transport transport) {
        LOGGER.debug("update(transport:{})", transport);
        return namedParameterJdbcTemplate.update(updateSql, new BeanPropertySqlParameterSource(transport));
    }

    @Override
    public int delete(Integer transportId) {
        LOGGER.debug("delete(id:{})",transportId);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(TRANSPORT_ID, transportId);
        return namedParameterJdbcTemplate.update(deleteSql, parameters);
    }
}
