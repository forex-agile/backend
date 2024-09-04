package com.fdmgroup.forex.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.forex.exceptions.InsufficientFundsException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.PortfolioAsset;
import com.fdmgroup.forex.repos.PortfolioAssetRepo;

public class PortfolioAssetServiceTest {

    @Mock
    private PortfolioAssetRepo portfolioAssetRepo;

    @InjectMocks
    private PortfolioAssetService portfolioAssetService;

    private UUID existingId;
    private UUID nonExistingId;
    private PortfolioAsset portfolioAsset;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingId = UUID.randomUUID();
        nonExistingId = UUID.randomUUID();
        portfolioAsset = new PortfolioAsset();
    }

    @Test
    void testPortfolioServiceInitialization() {
        verify(portfolioAssetRepo, times(0)).save(null);
    }

    @Test
    void testCreatePortfolioAsset_WithDefaultBalance() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, currency, 0.0);

        when(portfolioAssetRepo.save(any(PortfolioAsset.class))).thenReturn(portfolioAsset);

        PortfolioAsset createdPortfolioAsset = portfolioAssetService.createPortfolioAsset(portfolio, currency);

        assertEquals(0.0, createdPortfolioAsset.getBalance(), "The default balance should be 0.0");
        assertEquals(portfolio, createdPortfolioAsset.getPortfolio(), "The portfolio should match");
        assertEquals(currency, createdPortfolioAsset.getCurrency(), "The currency should match");
        verify(portfolioAssetRepo, times(1)).save(argThat(savedAsset -> savedAsset.getBalance() == 0.0 &&
                savedAsset.getPortfolio().equals(portfolio) &&
                savedAsset.getCurrency().equals(currency)));
    }

    @Test
    void testCreatePortfolioAsset_WithSpecifiedBalance() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double balance = 100.0;
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, currency, balance);

        when(portfolioAssetRepo.save(any(PortfolioAsset.class))).thenReturn(portfolioAsset);

        PortfolioAsset createdPortfolioAsset = portfolioAssetService.createPortfolioAsset(portfolio, currency, balance);

        assertEquals(balance, createdPortfolioAsset.getBalance(), "The balance should match the specified value");
        assertEquals(portfolio, createdPortfolioAsset.getPortfolio(), "The portfolio should match");
        assertEquals(currency, createdPortfolioAsset.getCurrency(), "The currency should match");
        verify(portfolioAssetRepo, times(1)).save(argThat(savedAsset -> savedAsset.getBalance() == balance &&
                savedAsset.getPortfolio().equals(portfolio) &&
                savedAsset.getCurrency().equals(currency)));
    }

    @Test
    void testFindById_WhenPortfolioAssetExists() {
        when(portfolioAssetRepo.findById(existingId)).thenReturn(Optional.of(portfolioAsset));
        PortfolioAsset foundPortfolioAsset = portfolioAssetService.findPortfolioAssetById(existingId);
        assertEquals(portfolioAsset, foundPortfolioAsset, "PortfolioAssetService should find a valid ID");
    }

    @Test
    void testFindById_WhenPortfolioAssetDoesNotExist() throws RecordNotFoundException {
        when(portfolioAssetRepo.findById(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            portfolioAssetService.findPortfolioAssetById(nonExistingId);
        }, "PortfolioAssetService should throw exception for an invalid ID");
    }

    @Test
    void testFindPortfolioAssetByPortfolioId() {
        when(portfolioAssetRepo.findByPortfolio_Id(existingId)).thenReturn(List.of(portfolioAsset));

        List<PortfolioAsset> foundPortfolioAssets = portfolioAssetService.findPortfolioAssetByPortfolioId(existingId);

        assertNotNull(foundPortfolioAssets, "PortfolioAssets should not be null");
        assertFalse(foundPortfolioAssets.isEmpty(), "PortfolioAssets should not be empty");
        assertEquals(portfolioAsset, foundPortfolioAssets.get(0),
                "The found portfolio asset should match the expected one");
    }

    @Test
    void testFindPortfolioAssetByPortfolioId_WhenPortfolioIdDoesNotExist() {
        UUID nonExistingId = UUID.randomUUID(); // This ID should not be present in the repo
        when(portfolioAssetRepo.findByPortfolio_Id(nonExistingId)).thenReturn(List.of());

        List<PortfolioAsset> foundPortfolioAssets = portfolioAssetService
                .findPortfolioAssetByPortfolioId(nonExistingId);

        assertNotNull(foundPortfolioAssets, "PortfolioAssets should not be null");
        assertTrue(foundPortfolioAssets.isEmpty(), "PortfolioAssets should be empty for non-existing ID");
    }

    @Test
    void testDepositAsset_WhenPortfolioAssetExists() throws RecordNotFoundException {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double initialBalance = 100.0;
        double deposit = 50.0;
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, currency, initialBalance);

        when(portfolioAssetRepo.findByPortfolioAndCurrency(portfolio, currency))
                .thenReturn(Optional.of(portfolioAsset));

        portfolioAssetService.depositAsset(portfolio, currency, deposit);

        assertEquals(initialBalance + deposit, portfolioAsset.getBalance(), "Balance should be updated after deposit");
        verify(portfolioAssetRepo, times(1)).save(portfolioAsset);
    }

    @Test
    void testDepositAsset_WhenPortfolioAssetDoesNotExist() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double deposit = 50.0;

        when(portfolioAssetRepo.findByPortfolioAndCurrency(portfolio, currency)).thenReturn(Optional.empty());

        portfolioAssetService.depositAsset(portfolio, currency, deposit);

        verify(portfolioAssetRepo, times(1)).save(any(PortfolioAsset.class));
    }

    @Test
    void testWithdrawAsset_WhenPortfolioAssetExists_WithSufficientFunds()
            throws RecordNotFoundException, InsufficientFundsException {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double initialBalance = 100.0;
        double withdrawal = 50.0;
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, currency, initialBalance);

        when(portfolioAssetRepo.findByPortfolioAndCurrency(portfolio, currency))
                .thenReturn(Optional.of(portfolioAsset));

        portfolioAssetService.withdrawAsset(portfolio, currency, withdrawal);

        assertEquals(initialBalance - withdrawal, portfolioAsset.getBalance(),
                "Balance should be updated after withdrawal");
        verify(portfolioAssetRepo, times(1))
                .save(argThat(savedAsset -> savedAsset.getBalance() == (initialBalance - withdrawal) &&
                        savedAsset.getPortfolio().equals(portfolio) &&
                        savedAsset.getCurrency().equals(currency)));
    }

    @Test
    void testWithdrawAsset_WhenPortfolioAssetExists_WithInsufficientFunds() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double initialBalance = 50.0;
        double withdrawal = 100.0;
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, currency, initialBalance);

        when(portfolioAssetRepo.findByPortfolioAndCurrency(portfolio, currency))
                .thenReturn(Optional.of(portfolioAsset));

        assertThrows(InsufficientFundsException.class, () -> {
            portfolioAssetService.withdrawAsset(portfolio, currency, withdrawal);
        }, "InsufficientFundsException should be thrown when attempting to withdraw more than the balance");
    }

}
