package com.clinic.user_service.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.clinic.common.AuditablePersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_APP_USER")
@Getter
@Setter
@RequiredArgsConstructor
public class User extends AuditablePersistableEntity implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Column(name = "EMAIL", nullable = false, unique = true)
	private String email;
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return (Collection<? extends GrantedAuthority>) roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
