package com.clinic.doctor.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DOCTOR_SCHEDULE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSchedule extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID")
	private Doctor doctor;

	@Column(name = "DAY_OF_WEEK")
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	@Column(name = "START_TIME")
	private LocalTime startTime;

	@Column(name = "END_TIME")
	private LocalTime endTime;
}
