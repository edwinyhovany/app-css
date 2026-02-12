package com.ccs.monitoring.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public class Coordinates {
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    
    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        validateLatitude(latitude);
        validateLongitude(longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    private void validateLatitude(BigDecimal latitude) {
        if (latitude == null) {
            throw new IllegalArgumentException("Latitude cannot be null");
        }
        if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 || 
            latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
    }
    
    private void validateLongitude(BigDecimal longitude) {
        if (longitude == null) {
            throw new IllegalArgumentException("Longitude cannot be null");
        }
        if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 || 
            longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
    
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(latitude, that.latitude) &&
               Objects.equals(longitude, that.longitude);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
    
    @Override
    public String toString() {
        return String.format("Coordinates(lat=%s, lon=%s)", latitude, longitude);
    }
}