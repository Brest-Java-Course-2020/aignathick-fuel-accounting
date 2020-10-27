package com.epam.brest.courses.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * POJO Transport for model.
 */
public class Transport {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String EUROPE_MINSK = "Europe/Minsk";
    /**
     * Transport Id.
     */
    private Integer transportId;
    /**
     * Transport Name.
     */
    private String transportName;
    /**
     * FK Fuel Id.
     */
    private Integer fuelId;
    /**
     * Transport tank capasiry.
     */
    private Double transportTankCapasity;
    /**
     * Transport Date.
     */
    @DateTimeFormat(pattern = DATE_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = EUROPE_MINSK)
    private Date transportDate;

    /**
     * Returns <code>Integer</code> representation of this transport Id.
     * @return transportId Transport Id.
     */
    public Integer getTransportId() {
        return transportId;
    }

    /**
     * Set transport identifier.
     * @param transportId Transport Id.
     * @return this Transport.
     */
    public Transport setTransportId(Integer transportId) {
        this.transportId = transportId;
        return this;
    }

    /**
     * Return <code>Integer</code> representation of this transport name.
     * @return transportName Transpor Name.
     */
    public String getTransportName() {
        return transportName;
    }

    /**
     * Set transport name.
     * @param transportName Transport Name.
     * @return this Transport.
     */
    public Transport setTransportName(String transportName) {
        this.transportName = transportName;
        return this;
    }

    /**
     * Returns <code>Integer</code> representation of this fuelId.
     * @return fuelId Fuel Id.
     */
    public Integer getFuelId() {
        return fuelId;
    }

    /**
     * Set the fuel's identifier.
     * @param fuelId Fuel Id.
     * @return this Transport.
     */
    public Transport setFuelId(Integer fuelId) {
        this.fuelId = fuelId;
        return this;
    }

    /**
     * Return <code>Double</code> representation of this Transport tank capasity.
     * @return transportTankCapasity transport tank capasity.
     *
     */
    public Double getTransportTankCapasity() {
        return transportTankCapasity;
    }

    /**
     * Set the transport's fuel tank capasity.
     * @param transportTankCapasity Transport fuel tank capasity.
     * @return this Transport
     */
    public Transport setTransportTankCapasity(Double transportTankCapasity) {
        this.transportTankCapasity = transportTankCapasity;
        return this;
    }

    /**
     * Return <code>Date</code> representation of Transport Date.
     * @return transportDate Transport Date.
     */
    public Date getTransportDate() {
        return transportDate;
    }

    /**
     * Set transport's Date.
     * @param transportDate Transport Date.
     * @return this Transport.
     */
    public Transport setTransportDate(Date transportDate) {
        this.transportDate = transportDate;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Transport{" +
                "transportId=" + transportId +
                ", transportName='" + transportName + '\'' +
                ", fuelId=" + fuelId +
                ", transportFuelTankCapasity=" + transportTankCapasity +
                ", transportDate=" + transportDate +
                '}';
    }
}
