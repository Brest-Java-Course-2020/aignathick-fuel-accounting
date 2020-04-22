package com.epam.brest.courses.model;

import java.util.Objects;

/**
 * POJO Fuel for model.
 */
public class Fuel {
    /**
     * Fuel Id.
     */
    private Integer fuelId;
    /**
     * Fuel Name.
     */
    private String fuelName;

    /**
     * Returns <code>Integer</code> representation of this fuelId.
     *
     * @return fuelId Fuel Id.
     */
    public Integer getFuelId() {
        return fuelId;
    }

    /**
     * Set the fuel's identifier.
     *
     * @param fuelId Fuel Id.
     * @return this Fuel.
     */
    public Fuel setFuelId(Integer fuelId) {
        this.fuelId = fuelId;
        return this;
    }

    /**
     * Returns <code>String</code> representation of this fuelName.
     *
     * @return fuelName Fuel Name.
     */
    public String getFuelName() {
        return fuelName;
    }

    /**
     * Set the fuel's name.
     *
     * @param fuelName Fuel Name.
     * @return this Fuel.
     */
    public Fuel setFuelName(String fuelName) {
        this.fuelName = fuelName;
        return this;
    }

    @Override
    public String toString() {
        return "Fuel{" +
                "fuelId=" + fuelId +
                ", fuelName='" + fuelName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fuel fuel = (Fuel) o;
        return Objects.equals(fuelId, fuel.fuelId) &&
                Objects.equals(fuelName, fuel.fuelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fuelId, fuelName);
    }
}
