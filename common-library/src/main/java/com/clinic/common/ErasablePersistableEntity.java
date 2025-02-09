package com.clinic.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
public abstract class ErasablePersistableEntity extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "DELETED", nullable = false)
	private boolean deleted = false;

	public void markAsDeleted() {
		this.deleted = true;
	}
}
