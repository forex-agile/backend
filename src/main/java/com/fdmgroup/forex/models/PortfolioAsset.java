package com.fdmgroup.forex.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import com.fdmgroup.forex.models.PortfolioAsset;

@Entity
public class PortfolioAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "FK_Portfolio_ID", nullable = false)
    private Portfolio portfolio;

    @ManyToMany()
    @JoinColumn(name = "FK_Currency_Code", nullable = false)
    private Currency currency;

    private double balance;

    public PortfolioAsset() {}

    public PortfolioAsset(Portfolio portfolio, Currency currency, double balance) {
        this.portfolio = portfolio;
        this.currency = currency;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID newId) {
        if (newId != null) {
            this.id = newId;
        }
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio newPortfolio) {
        if (newPortfolio != null) {
            this.portfolio = newPortfolio;
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency newCurrency) {
        if (newCurrency != null) {
            this.currency = newCurrency;
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
