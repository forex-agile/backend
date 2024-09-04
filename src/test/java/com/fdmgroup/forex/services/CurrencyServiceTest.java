package com.fdmgroup.forex.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.repos.CurrencyRepo;

public class CurrencyServiceTest {

    @Mock
    private CurrencyRepo currencyRepo;

    @InjectMocks
    private CurrencyService currencyService;

    private String validId;
    private String validName;
    private String invalidId;
    private Currency currency;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validId = "USD";
        validName = "U.S. Dollars";
        invalidId = "ABC";
        currency = new Currency(validId, validName);
    }

    @Test
    void testCurrencyServiceInitialization() {
        verify(currencyRepo, times(0)).save(null);
    }

    @Test
    void testFindAllCurrencies() {
        List<Currency> currencies = Arrays.asList(
            new Currency("JPY", "Japanese Yen"),
            new Currency("EUR", "Euros")
        );
        when(currencyRepo.findAll()).thenReturn(currencies);
        List<Currency> foundCurrencies = currencyService.findAllCurrencies();

        assertNotNull(foundCurrencies, "List of currencies should not be empty");
        assertEquals(2, foundCurrencies.size(), "Number of currencies should match");
        verify(currencyRepo, times(1)).findAll();
    }

    @Test
    void testFindById_WhenCurrencyExists() {
        when(currencyRepo.findById(validId)).thenReturn(Optional.of(currency));
        Currency foundCurrency = currencyService.findById(validId);
        assertEquals(currency, foundCurrency, "CurrencyService should find a valid currency ID");
    }

    @Test
    void testFindById_WhenCurrencyDoesNotExist() throws RecordNotFoundException {
        when(currencyRepo.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            currencyService.findById(invalidId);
        }, "CurrencyService should throw exception when searching for an invalid currency ID");
    }

}
