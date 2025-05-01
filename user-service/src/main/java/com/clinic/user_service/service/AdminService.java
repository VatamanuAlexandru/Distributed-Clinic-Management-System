package com.clinic.user_service.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.user_service.client.DoctorClient;
import com.clinic.user_service.dto.AdminRegisterRequest;
import com.clinic.user_service.dto.AuthRequest;
import com.clinic.user_service.entity.Person;
import com.clinic.user_service.entity.Role;
import com.clinic.user_service.entity.User;
import com.clinic.user_service.repository.RoleRepository;
import com.clinic.user_service.repository.UserRepository;

@Service
public class AdminService extends BaseService<User> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DoctorClient doctorClient;

	@Override
	protected JpaRepository<User, Long> getRepository() {
		return userRepository;
	}

	public User createAdminUser(AdminRegisterRequest adminRegisterRequest) {
		Optional<User> existingAdmin = userRepository.findByEmail(adminRegisterRequest.getEmail());
		if (existingAdmin.isPresent()) {
			throw new RuntimeException("User exist");
		}
		Role adminRole = roleRepository.findByName("ADMIN")
				.orElseThrow(() -> new RuntimeException("Admin role not found!"));

		User admin = new User();
		admin.setEmail(adminRegisterRequest.getEmail());
		admin.setPassword(passwordEncoder.encode(adminRegisterRequest.getPassword()));
		admin.setRoles(Set.of(adminRole));
		admin.setPerson(new Person());
		return userRepository.save(admin);

	}

	public User createDoctorUser(AuthRequest request) {
		@SuppressWarnings("unused")
		User existingDoctor = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User exists"));

		Role doctorRole = roleRepository.findByName(Role.DOCTOR_IDENTIFIER)
				.orElseThrow(() -> new RuntimeException("Doctor role not found"));

		User doctor = new User();
		doctor.setEmail(request.getEmail());
		doctor.setPassword(passwordEncoder.encode(request.getPassword()));
		doctor.setRoles(Set.of(doctorRole));
		User newUser = userRepository.save(doctor);
		doctorClient.createDoctor(newUser.getPerson().getId());
		return newUser;
	}

}
