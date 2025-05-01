package com.clinic.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.mapper.ModelMapper;
import com.clinic.user_service.client.PatientClient;
import com.clinic.user_service.dto.AuthRequest;
import com.clinic.user_service.dto.AuthResponse;
import com.clinic.user_service.entity.Role;
import com.clinic.user_service.entity.User;
import com.clinic.user_service.repository.RoleRepository;
import com.clinic.user_service.security.JwtService;
import com.clinic.user_service.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired(required = true)
	private ModelMapper modelMapper;

	@Autowired
	private PatientClient patientClient;
	
	@Autowired
	private RoleRepository roleRepository;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
			User user = userService.findByEmail(authRequest.getEmail()).orElseThrow();
			String token = jwtService.generateToken(user.getUsername());
			return ResponseEntity.ok(new AuthResponse(token));
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			return ResponseEntity.status(401).body("Invalid credentials");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
		User user = modelMapper.convertToEntity(authRequest, User.class);
		user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
		Role defaultRole = roleRepository.findByName(Role.PACIENT_IDENTIFIER)
				.orElseThrow(() -> new RuntimeException("Default role PACIENT not found"));
		user.getRoles().add(defaultRole);
		User savedUser = userService.save(user);
		patientClient.createPatient(savedUser.getPerson().getId());
		return ResponseEntity.ok(savedUser);
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from User Service!";
	}

}
