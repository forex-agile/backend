package com.fdmgroup.forex.models;

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
    @JoinColumn(name = "FK_User_ID")
    private UUID userId;

    public Portfolio() {}

    public Portfolio(UUID userId) {
        super();
        setUserId(userId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID newId) {
        if (newId != null) {
            this.id = newId;
        }
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID newUserId) {
        if (newUserId != null) {
            this.userId = newUserId;
        }
    }

    public String toString() {
        return "Portfolio [id=" + id + ", userId=" + userId + "]";
    }

}
