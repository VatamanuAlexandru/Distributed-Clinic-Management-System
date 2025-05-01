package com.clinic.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.user_service.dto.AdminRegisterRequest;
import com.clinic.user_service.dto.AuthRequest;
import com.clinic.user_service.entity.User;
import com.clinic.user_service.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/register-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterRequest request) {
		User admin = adminService.createAdminUser(request);
		return ResponseEntity.ok("Admin registered successfully with email: " + admin.getEmail());
	}

	@PostMapping("/register-doctor")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registerDoctor(@RequestBody AuthRequest request) {
		User doctor = adminService.createDoctorUser(request);
		return ResponseEntity.ok("Doctor registered succesufully with email: " + doctor.getEmail());
	}
}
