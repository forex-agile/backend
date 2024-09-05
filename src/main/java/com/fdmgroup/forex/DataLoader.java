package com.fdmgroup.forex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.fdmgroup.forex.models.Role;
import com.fdmgroup.forex.repos.RoleRepo;

import jakarta.transaction.Transactional;

@Component
public class DataLoader implements ApplicationRunner {

	@Value("${forex.default.role}")
	private String defaultRole;

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
		if (roleRepo.findByRole(defaultRole).isEmpty())
			roleRepo.save(new Role(defaultRole));
	}

}
