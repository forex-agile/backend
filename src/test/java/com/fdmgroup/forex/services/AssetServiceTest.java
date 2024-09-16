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
import com.fdmgroup.forex.models.Asset;
import com.fdmgroup.forex.repos.AssetRepo;

public class AssetServiceTest {

    @Mock
    private AssetRepo assetRepo;

    @InjectMocks
    private AssetService assetService;

    private UUID existingId;
    private UUID nonExistingId;
    private Asset Asset;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingId = UUID.randomUUID();
        nonExistingId = UUID.randomUUID();
        Asset = new Asset();
    }

    @Test
    void testAssetServiceInitialization() {
        verify(assetRepo, times(0)).save(null);
    }

    @Test
    void testCreateAsset_WithDefaultBalance() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        Asset Asset = new Asset(portfolio, currency, 0.0);

        when(assetRepo.save(any(Asset.class))).thenReturn(Asset);

        Asset createdAsset = assetService.createAsset(portfolio, currency);

        assertEquals(0.0, createdAsset.getBalance(), "The default balance should be 0.0");
        assertEquals(portfolio, createdAsset.getPortfolio(), "The portfolio should match");
        assertEquals(currency, createdAsset.getCurrency(), "The currency should match");
        verify(assetRepo, times(1)).save(argThat(savedAsset -> savedAsset.getBalance() == 0.0 &&
                savedAsset.getPortfolio().equals(portfolio) &&
                savedAsset.getCurrency().equals(currency)));
    }

    @Test
    void testCreateAsset_WithSpecifiedBalance() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double balance = 100.0;
        Asset Asset = new Asset(portfolio, currency, balance);

        when(assetRepo.save(any(Asset.class))).thenReturn(Asset);

        Asset createdAsset = assetService.createAsset(portfolio, currency, balance);

        assertEquals(balance, createdAsset.getBalance(), "The balance should match the specified value");
        assertEquals(portfolio, createdAsset.getPortfolio(), "The portfolio should match");
        assertEquals(currency, createdAsset.getCurrency(), "The currency should match");
        verify(assetRepo, times(1)).save(argThat(savedAsset -> savedAsset.getBalance() == balance &&
                savedAsset.getPortfolio().equals(portfolio) &&
                savedAsset.getCurrency().equals(currency)));
    }

    @Test
    void testFindById_WhenAssetExists() {
        when(assetRepo.findById(existingId)).thenReturn(Optional.of(Asset));
        Asset foundAsset = assetService.findAssetById(existingId);
        assertEquals(Asset, foundAsset, "AssetService should find a valid ID");
    }

    @Test
    void testFindById_WhenAssetDoesNotExist() throws RecordNotFoundException {
        when(assetRepo.findById(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            assetService.findAssetById(nonExistingId);
        }, "AssetService should throw exception for an invalid ID");
    }

    @Test
    void testFindAssetByPortfolioId() {
        when(assetRepo.findByPortfolio_Id(existingId)).thenReturn(List.of(Asset));

        List<Asset> foundAssets = assetService.findAssetByPortfolioId(existingId);

        assertNotNull(foundAssets, "Assets should not be null");
        assertFalse(foundAssets.isEmpty(), "Assets should not be empty");
        assertEquals(Asset, foundAssets.get(0),
                "The found portfolio asset should match the expected one");
    }

    @Test
    void testFindAssetByPortfolioId_WhenPortfolioIdDoesNotExist() {
        UUID nonExistingId = UUID.randomUUID(); // This ID should not be present in the repo
        when(assetRepo.findByPortfolio_Id(nonExistingId)).thenReturn(List.of());

        List<Asset> foundAssets = assetService
                .findAssetByPortfolioId(nonExistingId);

        assertNotNull(foundAssets, "Assets should not be null");
        assertTrue(foundAssets.isEmpty(), "Assets should be empty for non-existing ID");
    }

    @Test
    void testDepositAsset_WhenAssetExists() throws RecordNotFoundException {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double initialBalance = 100.0;
        double deposit = 50.0;
        Asset Asset = new Asset(portfolio, currency, initialBalance);

        when(assetRepo.findByPortfolioAndCurrency(portfolio, currency))
                .thenReturn(Optional.of(Asset));

        assetService.depositAsset(portfolio, currency, deposit);

        assertEquals(initialBalance + deposit, Asset.getBalance(), "Balance should be updated after deposit");
        verify(assetRepo, times(1)).save(Asset);
    }

    @Test
    void testDepositAsset_WhenAssetDoesNotExist() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double deposit = 50.0;

        when(assetRepo.findByPortfolioAndCurrency(portfolio, currency)).thenReturn(Optional.empty());

        assetService.depositAsset(portfolio, currency, deposit);

        verify(assetRepo, times(1)).save(any(Asset.class));
    }

    @Test
    void testWithdrawAsset_WhenAssetExists_WithSufficientFunds()
            throws RecordNotFoundException, InsufficientFundsException {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double initialBalance = 100.0;
        double withdrawal = 50.0;
        Asset Asset = new Asset(portfolio, currency, initialBalance);

        when(assetRepo.findByPortfolioAndCurrency(portfolio, currency))
                .thenReturn(Optional.of(Asset));

        assetService.withdrawAsset(portfolio, currency, withdrawal);

        assertEquals(initialBalance - withdrawal, Asset.getBalance(),
                "Balance should be updated after withdrawal");
        verify(assetRepo, times(1))
                .save(argThat(savedAsset -> savedAsset.getBalance() == (initialBalance - withdrawal) &&
                        savedAsset.getPortfolio().equals(portfolio) &&
                        savedAsset.getCurrency().equals(currency)));
    }

    @Test
    void testWithdrawAsset_WhenAssetExists_WithInsufficientFunds() {
        Portfolio portfolio = mock(Portfolio.class);
        Currency currency = mock(Currency.class);
        double initialBalance = 50.0;
        double withdrawal = 100.0;
        Asset Asset = new Asset(portfolio, currency, initialBalance);

        when(assetRepo.findByPortfolioAndCurrency(portfolio, currency))
                .thenReturn(Optional.of(Asset));

        assertThrows(InsufficientFundsException.class, () -> {
            assetService.withdrawAsset(portfolio, currency, withdrawal);
        }, "InsufficientFundsException should be thrown when attempting to withdraw more than the balance");
    }

}
