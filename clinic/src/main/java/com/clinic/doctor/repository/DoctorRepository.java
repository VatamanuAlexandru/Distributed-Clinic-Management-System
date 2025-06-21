package com.clinic.doctor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clinic.doctor.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	@Query("SELECT d FROM Doctor d  " + "JOIN MedicalDepartment md ON md.doctor.id = d.id "
			+ "WHERE md.department.id = ?1")
	List<Doctor> findByDepartmentId(Long departmentId);

	@Query("SELECT d FROM Doctor d " + "JOIN d.services ms " + "WHERE ms.id = ?1")
	List<Doctor> findByServiceId(Long serviceId);

	@Query("SELECT d FROM Doctor d where d.id IN (?1)")
	List<Doctor> findByIds(List<Long> ids);
	
	Doctor findByPersonId(Long personId);
}
