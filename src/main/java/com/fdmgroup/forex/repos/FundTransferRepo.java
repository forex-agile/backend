package com.fdmgroup.forex.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.*;

@Repository
public interface FundTransferRepo extends JpaRepository<FundTransfer, UUID> {
	List<FundTransfer> findAllTransfersByPortfolio(Portfolio portfolio);
}
