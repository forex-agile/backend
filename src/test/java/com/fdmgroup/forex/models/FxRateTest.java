package com.fdmgroup.forex.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FxRateTest {

    private Currency currency;
    private FxRate fxRate;

    @BeforeEach
    public void setUp() {
        currency = new Currency("HKD", "Hong Kong Dollars");
        fxRate = new FxRate(currency, 0.1274083493873);
    }

    @Test
    public void testFxRate_DefaultConstructor() {
        FxRate defaultFxRate = new FxRate();
        assertNull(defaultFxRate.getId(), "FxRate ID should be null");
        assertNull(defaultFxRate.getCurrency(), "FxRate currency should be null");
    }

    @Test
    public void testFxRate_ParameterizedConstructor() {
        assertEquals(currency, fxRate.getCurrency(), "FxRate currency should match");
        assertEquals(0.1274083493873, fxRate.getRateToUSD(), "FxRate rateToUSD should match");
    }

    @Test
    public void testSettersAndGetters() {
        Currency newCurrency = new Currency("EUR", "Euros");
        double newRateToUSD = 1.0148627714936;
        fxRate.setCurrency(newCurrency);
        fxRate.setRateToUSD(newRateToUSD);
        assertEquals(newCurrency, fxRate.getCurrency(), "FxRate currency should match");
        assertEquals(newRateToUSD, fxRate.getRateToUSD(), "FxRate rateToUSD should match");
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        fxRate.setCurrency(null);
        assertEquals(currency, fxRate.getCurrency(), "FxRate currency should be unchanged");
    }

}
