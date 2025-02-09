package com.clinic.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedPersistableEntity extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", nullable = false)
	private String name;
}
