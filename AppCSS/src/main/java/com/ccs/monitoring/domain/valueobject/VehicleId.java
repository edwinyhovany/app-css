package com.ccs.monitoring.domain.valueobject;

import java.util.Objects;

/**
 * Value Object para identificador de vehículo
 * Inmutable, sin dependencias de frameworks
 */
public final class VehicleId {
    private final String value;
    
    public VehicleId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("VehicleId cannot be null or empty");
        }
        this.value = value.trim().toUpperCase();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleId vehicleId = (VehicleId) o;
        return Objects.equals(value, vehicleId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}