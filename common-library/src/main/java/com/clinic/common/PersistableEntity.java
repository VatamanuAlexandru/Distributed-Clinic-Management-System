package com.clinic.common;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for persistent entities.
 * 
 * <p>
 * This class provides a common structure for all entities in the system,
 * ensuring consistency and reusability. It includes:
 * </p>
 * 
 * <ul>
 * <li>A unique identifier (<code>id</code>) with automatic generation.</li>
 * <li>Optimistic locking using the <code>version</code> field to prevent data
 * conflicts.</li>
 * <li>Overridden <code>equals()</code> and <code>hashCode()</code> methods
 * based on <code>id</code>.</li>
 * </ul>
 * 
 * <p>
 * This class should be extended by all entities that require persistence.
 * </p>
 */
@MappedSuperclass
@Getter
@Setter
public class PersistableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Version
	@Column(name = "VERSION")
	protected int version;

	@Column(name = "ENABLED")
	private boolean enabled = true;

	@Column(name = "SYSTEM")
	private boolean system = false;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PersistableEntity objectToCompare = (PersistableEntity) o;
		return id != null && id.equals(objectToCompare.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
