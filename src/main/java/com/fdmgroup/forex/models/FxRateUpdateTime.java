package com.fdmgroup.forex.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "fx_rate_update_time")
public class FxRateUpdateTime {

    @Id
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime lastUpdateTime;

    public FxRateUpdateTime() {
        this.id = 1;
        this.lastUpdateTime = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
