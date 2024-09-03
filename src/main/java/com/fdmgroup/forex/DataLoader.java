package com.fdmgroup.forex;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.fdmgroup.forex.models.Role;
import com.fdmgroup.forex.repos.RoleRepo;

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

	private void createRoles() {
		if (roleRepo.count() != 0)
			return;

		roleRepo.save(new Role(1L, "USER"));
	}

}
