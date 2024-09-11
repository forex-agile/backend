package com.fdmgroup.forex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.FxRateUpdateTime;

@Repository
public interface FxRateUpdateTimeRepo extends JpaRepository<FxRateUpdateTime, Integer> {

}
