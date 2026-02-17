package com.ccs.monitoring.domain.valueobject;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {
    @Test
    void shouldCreateValidCoordinates() {
        Coordinates coords = new Coordinates(BigDecimal.valueOf(4.6097), BigDecimal.valueOf(-74.0817));
        assertEquals(BigDecimal.valueOf(4.6097), coords.getLatitude());
        assertEquals(BigDecimal.valueOf(-74.0817), coords.getLongitude());
    }
    @Test
    void shouldRejectInvalidLatitude() {
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(BigDecimal.valueOf(100), BigDecimal.valueOf(0)));
    }
    @Test
    void shouldRejectInvalidLongitude() {
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(BigDecimal.valueOf(0), BigDecimal.valueOf(200)));
    }
    @Test
void shouldBeEqualWhenCoordinatesAreSame() {
    Coordinates c1 = new Coordinates(BigDecimal.valueOf(4.6), BigDecimal.valueOf(-74.0));
    Coordinates c2 = new Coordinates(BigDecimal.valueOf(4.6), BigDecimal.valueOf(-74.0));

    assertEquals(c1, c2);
    assertEquals(c1.hashCode(), c2.hashCode());
}

@Test
void shouldNotBeEqualWhenDifferent() {
    Coordinates c1 = new Coordinates(BigDecimal.valueOf(4.6), BigDecimal.valueOf(-74.0));
    Coordinates c2 = new Coordinates(BigDecimal.valueOf(5.0), BigDecimal.valueOf(-70.0));

    assertNotEquals(c1, c2);
}

@Test
void shouldNotBeEqualToNull() {
    Coordinates c1 = new Coordinates(BigDecimal.valueOf(4.6), BigDecimal.valueOf(-74.0));
    assertNotEquals(c1, null);
}

@Test
void shouldNotBeEqualToDifferentClass() {
    Coordinates c1 = new Coordinates(BigDecimal.valueOf(4.6), BigDecimal.valueOf(-74.0));
    assertNotEquals(c1, "not coordinates");
}

@Test
void shouldReturnStringRepresentation() {
    Coordinates c1 = new Coordinates(BigDecimal.valueOf(4.6), BigDecimal.valueOf(-74.0));

    String result = c1.toString();

    assertTrue(result.contains("lat="));
    assertTrue(result.contains("lon="));
}
    
}
