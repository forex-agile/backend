package com.fdmgroup.forex;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.fdmgroup.forex.models.Role;
import com.fdmgroup.forex.repos.RoleRepo;

import jakarta.transaction.Transactional;

@Component
public class DataLoader implements ApplicationRunner {

	private RoleRepo roleRepo;

	public DataLoader(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		createRoles();
	}

	@Transactional
	private void createRoles() {
		if (roleRepo.findByRole("USER").isEmpty())
			roleRepo.save(new Role("USER"));
	}

}
