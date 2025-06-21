package com.clinic.doctor.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.common.service.BaseService;
import com.clinic.doctor.dto.DoctorScheduleRecord;
import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.entity.DoctorSchedule;
import com.clinic.doctor.entity.MedicalService;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.doctor.repository.DoctorScheduleRepository;
import com.clinic.doctor.repository.MedicalServiceRepository;
import com.clinic.mapper.ModelMapper;

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

	@Autowired
	private ModelMapper modelMapper;

	@Override
	protected JpaRepository<DoctorSchedule, Long> getRepository() {
		return doctorScheduleRepository;
	}

	public List<LocalDateTime> getAvailableSlots(Long doctorId, Long serviceId, LocalDateTime date) {
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
		MedicalService service = medicalServiceRepository.findById(serviceId).orElseThrow();
		LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

		List<Appointment> existing = appointmentRepository.findByDoctorAndAppointmentDateBetween(doctor, startOfDay,
				endOfDay);
		List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctor(doctor);

		DayOfWeek day = date.getDayOfWeek();
		List<DoctorSchedule> todaySchedule = schedules.stream().filter(s -> s.getDayOfWeek() == day).toList();

		List<LocalDateTime> slots = new ArrayList<>();
		for (DoctorSchedule s : todaySchedule) {
			LocalTime time = s.getStartTime();
			while (time.plusMinutes(service.getDurationMinutes()).isBefore(s.getEndTime().plusSeconds(1))) {
				LocalDateTime candidate = LocalDateTime.of(date.toLocalDate(), time);
				boolean taken = existing.stream().anyMatch(app -> app.getAppointmentDate().equals(candidate));
				if (!taken) {
					slots.add(candidate);
				}
				time = time.plusMinutes(service.getDurationMinutes());
			}
		}

		return slots;
	}

	public List<LocalDateTime> getOcupiedSlots(Long doctorId, Long serviceId, LocalDateTime date) {
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
		MedicalService service = medicalServiceRepository.findById(serviceId).orElseThrow();
		LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

		List<Appointment> existing = appointmentRepository.findByDoctorAndAppointmentDateBetween(doctor, startOfDay,
				endOfDay);
		List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctor(doctor);

		DayOfWeek day = date.getDayOfWeek();
		List<DoctorSchedule> todaySchedule = schedules.stream().filter(s -> s.getDayOfWeek() == day).toList();

		List<LocalDateTime> occupiedSlots = new ArrayList<>();
		for (DoctorSchedule s : todaySchedule) {
			LocalTime time = s.getStartTime();
			while (time.plusMinutes(service.getDurationMinutes()).isBefore(s.getEndTime().plusSeconds(1))) {
				LocalDateTime candidate = LocalDateTime.of(date.toLocalDate(), time);
				boolean taken = existing.stream()
						.anyMatch(app -> ChronoUnit.MINUTES.between(app.getAppointmentDate(), candidate) == 0);
				if (taken) {
					occupiedSlots.add(candidate);
				}
				time = time.plusMinutes(service.getDurationMinutes());
			}
		}

		return occupiedSlots;
	}

	public List<DoctorScheduleRecord> getSchedulesByDoctor(Long doctorId) {
		Doctor doctor = doctorRepository.findById(doctorId)
				.orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + doctorId));

		List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctor(doctor);

		return schedules.stream().map(s -> modelMapper.convertToDto(s, DoctorScheduleRecord.class))
				.collect(Collectors.toList());
	}

	public DoctorScheduleRecord addSchedule(Long doctorId, DayOfWeek dayOfWeek, LocalTime startTime,
			LocalTime endTime) {
		List<DoctorSchedule> existingSchedules = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId,
				dayOfWeek);
		for (DoctorSchedule existing : existingSchedules) {
			boolean overlaps = startTime.isBefore(existing.getEndTime()) && endTime.isAfter(existing.getStartTime());
			if (overlaps) {
				throw new IllegalArgumentException("Intervalul se suprapune cu un interval existent.");
			}
		}
		DoctorSchedule newSchedule = new DoctorSchedule();
		newSchedule.setDoctor(doctorRepository.findById(doctorId).orElseThrow());
		newSchedule.setDayOfWeek(dayOfWeek);
		newSchedule.setStartTime(startTime);
		newSchedule.setEndTime(endTime);

		newSchedule = doctorScheduleRepository.save(newSchedule);

		return modelMapper.convertToDto(newSchedule, DoctorScheduleRecord.class);
	}

	public void deleteSchedule(Long scheduleId) {
		doctorScheduleRepository.deleteById(scheduleId);
	}

}
