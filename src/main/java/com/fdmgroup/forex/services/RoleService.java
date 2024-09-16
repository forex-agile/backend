package com.fdmgroup.forex.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.forex.models.Role;
import com.fdmgroup.forex.repos.RoleRepo;

/**
 * RoleService
 */
@Service
public class RoleService {

	@Autowired
	private RoleRepo roleRepo;

	public List<Role> getAllRoles() {
		return roleRepo.findAll();
	}

}
