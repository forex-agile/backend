package com.fdmgroup.forex.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.repos.PortfolioRepo;

public class PortfolioServiceTest {

    @Mock
    private PortfolioRepo portfolioRepo;

    @InjectMocks
    private PortfolioService portfolioService;

    private UUID existingId;
    private UUID nonExistingId;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingId = UUID.randomUUID();
        nonExistingId = UUID.randomUUID();
        portfolio = new Portfolio(existingId);
    }

    @Test
    void testPortfolioServiceInitialization() {
        verify(portfolioRepo, times(0)).save(null);
    }

    @Test
    void testFindById_WhenPortfolioExists() {
        when(portfolioRepo.findById(existingId)).thenReturn(Optional.of(portfolio));
        Portfolio foundPortfolio = portfolioService.findById(existingId);
        assertEquals(portfolio, foundPortfolio, "PortfolioService should find a valid ID");
    }

    @Test
    void testFindById_WhenPortfolioDoesNotExist() throws RecordNotFoundException {
        when(portfolioRepo.findById(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            portfolioService.findById(nonExistingId);
        }, "PortfolioService should throw exception for an invalid ID");
    }

    @Test
    void testFindByUserId_WhenPortfolioExists() {
        when(portfolioRepo.findByUserId(existingId)).thenReturn(Optional.of(portfolio));
        Portfolio foundPortfolio = portfolioService.findByUserId(existingId);
        assertEquals(portfolio, foundPortfolio, "PortfolioService should find a valid user ID");
    }

    @Test
    void testFindByUserId_WhenPortfolioDoesNotExist() throws RecordNotFoundException {
        when(portfolioRepo.findByUserId(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> {
            portfolioService.findByUserId(nonExistingId);
        }, "PortfolioService should throw exception for an invalid user ID");
    }
}
