package com.clinic.doctor.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.common.service.BaseService;
import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.entity.DoctorSchedule;
import com.clinic.doctor.entity.MedicalService;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.doctor.repository.DoctorScheduleRepository;
import com.clinic.doctor.repository.MedicalServiceRepository;

@Service
public class DoctorScheduleService extends BaseService<DoctorSchedule> {

	@Autowired
	private DoctorScheduleRepository doctorScheduleRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private MedicalServiceRepository medicalServiceRepository;

	@Override
	protected JpaRepository<DoctorSchedule, Long> getRepository() {
		return doctorScheduleRepository;
	}

//	public List<LocalDateTime> getAvailableSlots(Long doctorId, Long serviceId, LocalDate date) {
//		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
//		MedicalService service = medicalServiceRepository.findById(serviceId).orElseThrow();
//		List<Appointment> existing = appointmentRepository.findByDoctorAndDate(doctor, date);
//		List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctorId(doctorId);
//
//		DayOfWeek day = date.getDayOfWeek();
//		List<DoctorSchedule> todaySchedule = schedules.stream().filter(s -> s.getDayOfWeek() == day).toList();
//
//		List<LocalDateTime> slots = new ArrayList<>();
//		for (DoctorSchedule s : todaySchedule) {
//			LocalTime time = s.getStartTime();
//			while (time.plusMinutes(service.getDurationMinutes()).isBefore(s.getEndTime().plusSeconds(1))) {
//				LocalDateTime candidate = LocalDateTime.of(date, time);
//				boolean taken = existing.stream().anyMatch(app -> app.getAppointmentDate().equals(candidate));
//				if (!taken) {
//					slots.add(candidate);
//				}
//				time = time.plusMinutes(service.getDurationMinutes());
//			}
//		}
//
//		return slots;
//	}

}
