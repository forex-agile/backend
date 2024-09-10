package com.fdmgroup.forex.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.forex.models.FxRateUpdateTime;

public interface FxRateUpdateTimeRepo extends JpaRepository<FxRateUpdateTime, Integer> {

}
