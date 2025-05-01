package com.clinic.user_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.user_service.entity.User;
import com.clinic.user_service.repository.UserRepository;

@Service
public class UserService extends BaseService<User> implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	protected JpaRepository<User, Long> getRepository() {
		return userRepository;
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

	}
}
