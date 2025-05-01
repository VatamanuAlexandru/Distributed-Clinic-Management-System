package com.clinic.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.user_service.entity.Role;
import com.clinic.user_service.repository.RoleRepository;

@Service
public class RoleService extends BaseService<Role> {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	protected JpaRepository<Role, Long> getRepository() {
		return roleRepository;
	}

}
