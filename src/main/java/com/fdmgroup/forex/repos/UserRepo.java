package com.fdmgroup.forex.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.User;

/**
 * Repository for {@link com.fdmgroup.forex.models.User User} entity
 */
@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
}
