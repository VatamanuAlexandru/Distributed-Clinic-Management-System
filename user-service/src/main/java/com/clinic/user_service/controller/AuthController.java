package com.clinic.user_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.mapper.ModelMapper;
import com.clinic.user_service.client.ClinicClient;
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
	private RoleRepository roleRepository;

	@Autowired
	private ClinicClient clinicClient;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
			User user = userService.findByEmail(authRequest.getEmail()).orElseThrow();
			String token = jwtService.generateToken(user.getUsername());
			List<String> roleNames = user.getRoles().stream().map(role -> role.getName()).toList();
			return ResponseEntity.ok(new AuthResponse(token, roleNames));
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
		clinicClient.createPatient(savedUser.getPerson().getId());
		return ResponseEntity.ok(savedUser);
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from User Service!";
	}

	@GetMapping("/currentPatientId")
	public ResponseEntity<Long> getCurrentPatientId(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
		Long personId = user.getPerson().getId();
		Long patientId = clinicClient.getPatientIdByPersonId(personId);
		return ResponseEntity.ok(patientId);
	}

	@GetMapping("/currentDoctorId")
	public ResponseEntity<Long> getCurrentDoctorId(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
		Long personId = user.getPerson().getId();
		Long patientId = clinicClient.getDoctorIdByPersonId(personId);
		return ResponseEntity.ok(patientId);
	}

	@GetMapping("/getEmailAndUserIdByPersonId")
	public ResponseEntity<Map<String, Object>> getEmailAndUserIdByPersonId(@RequestParam Long personId) {
		User user = userService.findByPersonId(personId).orElseThrow();
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getId());
		map.put("email", user.getEmail());
		return ResponseEntity.ok(map);
	}

	@GetMapping("/currentUserId")
	public ResponseEntity<Long> getCurrentUserId(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
		return ResponseEntity.ok(user.getId());
	}

}
