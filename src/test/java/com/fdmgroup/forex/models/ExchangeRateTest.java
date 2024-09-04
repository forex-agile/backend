package com.fdmgroup.forex.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateTest {

    private Currency currency;
    private ExchangeRate exchangeRate;

    @BeforeEach
    public void setUp() {
        currency = new Currency("HKD", "Hong Kong Dollars");
        exchangeRate = new ExchangeRate(currency, 0.1274083493873);
    }

    @Test
    public void testExchangeRate_DefaultConstructor() {
        ExchangeRate defaultExchangeRate = new ExchangeRate();
        assertNull(defaultExchangeRate.getId(), "ExchangeRate ID should be null");
        assertNull(defaultExchangeRate.getCurrency(), "ExchangeRate currency should be null");
    }

    @Test
    public void testExchangeRate_ParameterizedConstructor() {
        assertEquals(currency, exchangeRate.getCurrency(), "ExchangeRate currency should match");
        assertEquals(0.1274083493873, exchangeRate.getRateToUSD(), "ExchangeRate rateToUSD should match");
    }

    @Test
    public void testSettersAndGetters() {
        Currency newCurrency = new Currency("EUR", "Euros");
        double newRateToUSD = 1.0148627714936;
        exchangeRate.setCurrency(newCurrency);
        exchangeRate.setRateToUSD(newRateToUSD);
        assertEquals(newCurrency, exchangeRate.getCurrency(), "ExchangeRate currency should match");
        assertEquals(newRateToUSD, exchangeRate.getRateToUSD(), "ExchangeRate rateToUSD should match");
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        exchangeRate.setCurrency(null);
        assertEquals(currency, exchangeRate.getCurrency(), "ExchangeRate currency should be unchanged");
    }

}
