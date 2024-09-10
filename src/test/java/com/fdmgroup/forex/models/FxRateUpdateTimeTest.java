package com.fdmgroup.forex.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FxRateUpdateTimeTest {

    private FxRateUpdateTime fxRateUpdateTime;

    @BeforeEach
    public void setUp() {
        fxRateUpdateTime = new FxRateUpdateTime();
    }

    @Test
    public void testFxRateUpdateTime_DefaultConstructor() {
        assertNotNull(fxRateUpdateTime.getLastUpdateTime());
    }

    @Test
    public void testSettersAndGetters() {
        LocalDateTime updatedTime = LocalDateTime.now();
        assertNotEquals(updatedTime, fxRateUpdateTime.getLastUpdateTime());
        fxRateUpdateTime.setLastUpdateTime(updatedTime);
        assertEquals(updatedTime, fxRateUpdateTime.getLastUpdateTime());
    }

}
