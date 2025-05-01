package com.clinic.administrative.entity;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "OFFICE")
@Getter
@Setter
@AllArgsConstructor
public class Office extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "ROOM_NUMBER")
	private String roomNumber;

	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	private Department department;

}
