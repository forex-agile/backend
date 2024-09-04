package com.fdmgroup.forex.models;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne()
    @JoinColumn(name = "FK_User_ID", nullable = false)
    private User user;

    private List<PortfolioAsset> portfolioAssets;

    public Portfolio() {}

    public Portfolio(User user, List<PortfolioAsset> portfolioAssets) {
        setUser(user);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID newId) {
        if (newId != null) {
            this.id = newId;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User newUser) {
        if (newUser != null) {
            this.user = newUser;
        }
    }

    public List<PortfolioAsset> getPortfolioAssets() {
        return portfolioAssets;
    }

    public void setPortfolioAssets(List<PortfolioAsset> newPortfolioAssets) {
        if (newPortfolioAssets != null) {
            this.portfolioAssets = newPortfolioAssets;
        }
    }

}
