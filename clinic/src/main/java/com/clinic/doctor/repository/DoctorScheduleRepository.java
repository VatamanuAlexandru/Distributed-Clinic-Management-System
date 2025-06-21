package com.clinic.doctor.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.entity.DoctorSchedule;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
	
	List<DoctorSchedule> findByDoctor(Doctor doctor);
	
	List<DoctorSchedule> findByDoctorId(Long doctorId);
	
	List<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

}
