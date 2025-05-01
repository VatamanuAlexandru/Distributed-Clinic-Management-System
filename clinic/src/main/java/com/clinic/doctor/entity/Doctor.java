package com.clinic.doctor.entity;

import java.util.List;

import com.clinic.administrative.entity.MedicalDepartment;
import com.clinic.administrative.entity.Office;
import com.clinic.common.PersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DOCTOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "PERSON_ID", unique = true)
	private Long personId;

	@ManyToOne
	@JoinColumn(name = "TITLE_ID")
	private MedicalTitle medicalTitle;

	@ManyToOne
	@JoinColumn(name = "RANK_ID")
	private MedicalRank medicalRank;

	@Column(name = "YEARS_OF_EXPERIENCE")
	private Integer yearsOfExpereience;

	@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
	private List<MedicalDepartment> medicalDepartments;

	@ManyToOne
	@JoinColumn(name = "OFFICE_ID")
	private Office office;
}
