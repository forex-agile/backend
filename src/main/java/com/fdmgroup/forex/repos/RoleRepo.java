package com.fdmgroup.forex.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.Role;

/**
 * Repository for {@link com.fdmgroup.forex.models.Role Role} entity
 */
@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
	Optional<Role> findByRole(String role);
}
