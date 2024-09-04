package com.fdmgroup.forex.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "FK_Currency_ID", nullable = false)
    private Currency currency;

    private double rateToUSD;

    public ExchangeRate() {}

    public ExchangeRate(Currency currency, double rateToUSD) {
        this.currency = currency;
        this.rateToUSD = rateToUSD;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID newId) {
        if (newId != null) {
            this.id = newId;
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

    public double getRateToUSD() {
        return rateToUSD;
    }

    public void setRateToUSD(double newRateToUSD) {
        this.rateToUSD = newRateToUSD;
    }

}
