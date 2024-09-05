package com.fdmgroup.forex.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {

    private Currency currency;


    @BeforeEach
    public void setUp() {
        currency = new Currency("USD", "U.S. Dollars");
    }

    @Test
    public void testCurrency_DefaultConstructor() {
        Currency defaultCurrency = new Currency();
        assertNull(defaultCurrency.getCurrencyCode(), "Currency code should be null");
        assertNull(defaultCurrency.getCurrencyName(), "Currency name should be null");
    }

    @Test
    public void testCurrency_ParameterizedConstructor() {
        assertNotNull(currency.getCurrencyCode(), "Currency code should not be null");
        assertEquals("USD", currency.getCurrencyCode(), "Currency code should match");
        assertNotNull(currency.getCurrencyName(), "Currency name should not be null");
        assertEquals("U.S. Dollars", currency.getCurrencyName(), "Currency name should match");
    }

    @Test
    public void testSettersAndGetters() {
        String newCode = "HKD";
        String newName = "Hong Kong Dollars";
        currency.setCurrencyCode(newCode);
        currency.setCurrencyName(newName);
        assertEquals(newCode, currency.getCurrencyCode(), "Currency code should match");
        assertEquals(newName, currency.getCurrencyName(), "Currency name should match");
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        String newCode = "";
        String newName = "";
        currency.setCurrencyCode(newCode);
        currency.setCurrencyName(newName);
        assertEquals("USD", currency.getCurrencyCode(), "Currency code should be unchanged");
        assertEquals("U.S. Dollars", currency.getCurrencyName(), "Currency name should be unchanged");
    }

    @Test
    public void testToString() {
        String expectedString = "Currency [code=USD, name=U.S. Dollars]";
        assertEquals(expectedString, currency.toString(), "toString should match expected output");
    }

}
