package com.fdmgroup.forex.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.ExchangeRate;
import com.fdmgroup.forex.repos.ExchangeRateRepo;

public class ExchangeRateServiceTest {

    @Mock
    ExchangeRateRepo exchangeRateRepo;

    @InjectMocks
    ExchangeRateService exchangeRateService;

    private Currency currency;
    private ExchangeRate exchangeRate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currency = new Currency("HKD", "Hong Kong Dollars");
        exchangeRate = new ExchangeRate(currency, 0.1274083493873);
    }

    @Test
    void testExchangeRateServiceInitialization() {
        verify(exchangeRateRepo, times(0)).save(null);
    }

    @Test
    void testFindAllExchangeRates() {
        Currency jpy = new Currency("JPY", "Japanese Yen");
        Currency eur = new Currency("EUR", "Euros");
        List<ExchangeRate> exchangeRates = Arrays.asList(
                new ExchangeRate(jpy, 0.007026275566888),
                new ExchangeRate(eur, 1.0148627714936));
        when(exchangeRateRepo.findAll()).thenReturn(exchangeRates);
        List<ExchangeRate> foundExchangeRates = exchangeRateService.findAllExchangeRates();

        assertNotNull(exchangeRates, "List of exchange rates should not be empty");
        assertEquals(2, foundExchangeRates.size(), "Number of exchange rates should match");
        verify(exchangeRateRepo, times(1)).findAll();
    }

    @Test
    void testFindExchangeRateById_WhenExchangeRateExists() {
        UUID newId = UUID.randomUUID();
        when(exchangeRateRepo.findById(newId)).thenReturn(Optional.of(exchangeRate));
        ExchangeRate foundExchangeRate = exchangeRateService.findExchangeRateById(newId);
        assertEquals(exchangeRate, foundExchangeRate, "ExchangeRateService should find a valid exchange rate ID");
    }

    @Test
    void testFindExchangeRateById_WhenExchangeRateDoesNotExist() throws RecordNotFoundException {
        UUID newId = UUID.randomUUID();
        when(exchangeRateRepo.findById(newId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            exchangeRateService.findExchangeRateById(newId);
        }, "ExchangeRateService should throw exception when searching for an invalid exchange rate ID");
    }

    @Test
    void testFindExchangeRateByCurrency_WhenExchangeRateExists() {
        when(exchangeRateRepo.findByCurrency(currency)).thenReturn(Optional.of(exchangeRate));
        ExchangeRate foundExchangeRate = exchangeRateService.findExchangeRateByCurrency(currency);
        assertEquals(exchangeRate, foundExchangeRate, "ExchangeRateService should find an exchange rate for a valid currency");
    }

    @Test
    void testFindExchangeRateByCurrency_WhenExchangeRateDoesNotExist() {
        Currency invalidCurrency = new Currency("ABC", "Fake Currency");
        when(exchangeRateRepo.findByCurrency(invalidCurrency)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            exchangeRateService.findExchangeRateByCurrency(invalidCurrency);
        }, "ExchangeRateService should throw exception when searching for an exchange rate with an invalid currency");
    }

}
