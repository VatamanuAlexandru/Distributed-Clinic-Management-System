package com.clinic.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.doctor.entity.DoctorSchedule;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
	
	

}
