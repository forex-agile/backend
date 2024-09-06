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
import com.fdmgroup.forex.models.FxRate;
import com.fdmgroup.forex.repos.FxRateRepo;

public class FxRateServiceTest {

    @Mock
    FxRateRepo fxRateRepo;

    @InjectMocks
    FxRateService fxRateService;

    private Currency currency;
    private FxRate fxRate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currency = new Currency("HKD", "Hong Kong Dollars");
        fxRate = new FxRate(currency, 0.1274083493873);
    }

    @Test
    void testFxRateServiceInitialization() {
        verify(fxRateRepo, times(0)).save(null);
    }

    @Test
    void testFindAllFxRates() {
        Currency jpy = new Currency("JPY", "Japanese Yen");
        Currency eur = new Currency("EUR", "Euros");
        List<FxRate> fxRates = Arrays.asList(
                new FxRate(jpy, 0.007026275566888),
                new FxRate(eur, 1.0148627714936));
        when(fxRateRepo.findAll()).thenReturn(fxRates);
        List<FxRate> foundFxRates = fxRateService.findAllFxRates();

        assertNotNull(fxRates, "List of exchange rates should not be empty");
        assertEquals(2, foundFxRates.size(), "Number of exchange rates should match");
        verify(fxRateRepo, times(1)).findAll();
    }

    @Test
    void testFindFxRateById_WhenFxRateExists() {
        UUID newId = UUID.randomUUID();
        when(fxRateRepo.findById(newId)).thenReturn(Optional.of(fxRate));
        FxRate foundFxRate = fxRateService.findFxRateById(newId);
        assertEquals(fxRate, foundFxRate, "FxRateService should find a valid exchange rate ID");
    }

    @Test
    void testFindFxRateById_WhenFxRateDoesNotExist() throws RecordNotFoundException {
        UUID newId = UUID.randomUUID();
        when(fxRateRepo.findById(newId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            fxRateService.findFxRateById(newId);
        }, "FxRateService should throw exception when searching for an invalid exchange rate ID");
    }

    @Test
    void testFindFxRateByCurrency_WhenFxRateExists() {
        when(fxRateRepo.findByCurrency_CurrencyId(currency.getCurrencyCode())).thenReturn(Optional.of(fxRate));
        FxRate foundFxRate = fxRateService.findFxRateByCurrencyId(currency.getCurrencyCode());
        assertEquals(fxRate, foundFxRate, "FxRateService should find an exchange rate for a valid currency");
    }

    @Test
    void testFindFxRateByCurrency_WhenFxRateDoesNotExist() {
        Currency invalidCurrency = new Currency("ABC", "Fake Currency");
        when(fxRateRepo.findByCurrency_CurrencyId(invalidCurrency.getCurrencyCode())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            fxRateService.findFxRateByCurrencyId(invalidCurrency.getCurrencyCode());
        }, "FxRateService should throw exception when searching for an exchange rate with an invalid currency");
    }

}
