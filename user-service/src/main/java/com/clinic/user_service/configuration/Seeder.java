package com.clinic.user_service.configuration;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.clinic.user_service.entity.Person;
import com.clinic.user_service.entity.Role;
import com.clinic.user_service.entity.User;
import com.clinic.user_service.repository.RoleRepository;
import com.clinic.user_service.repository.UserRepository;

@Component
public class Seeder implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		Role adminRole = roleRepository.findByName(Role.ADMIN_IDENTIFIER).orElseGet(() -> {
			Role role = new Role();
			role.setName(Role.ADMIN_IDENTIFIER);
			return roleRepository.save(role);
		});

		Role patientRole = roleRepository.findByName(Role.PACIENT_IDENTIFIER).orElseGet(() -> {
			Role role = new Role();
			role.setName(Role.PACIENT_IDENTIFIER);
			return roleRepository.save(role);
		});

		Role doctorRole = roleRepository.findByName(Role.DOCTOR_IDENTIFIER).orElseGet(() -> {
			Role role = new Role();
			role.setName(Role.DOCTOR_IDENTIFIER);
			return roleRepository.save(role);
		});

		Optional<User> adminUser = userRepository.findByEmail("admin@clinic.ro");
		if (!adminUser.isPresent()) {
			User admin = new User();
			admin.setEmail("admin@clinic.ro");
			admin.setPassword(passwordEncoder.encode("ffff"));
			admin.setRoles(Set.of(adminRole));
			admin.setPerson(new Person());
			admin.setEnabled(true);
			userRepository.save(admin);
		}

	}

}
