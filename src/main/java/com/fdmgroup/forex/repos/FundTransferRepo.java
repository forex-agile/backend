package com.fdmgroup.forex.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.FundTransfer;

@Repository
public interface FundTransferRepo extends JpaRepository<FundTransfer, UUID> {

}
