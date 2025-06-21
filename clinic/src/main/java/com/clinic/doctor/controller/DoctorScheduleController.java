package com.clinic.doctor.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.doctor.dto.DoctorScheduleRecord;
import com.clinic.doctor.entity.DoctorSchedule;
import com.clinic.doctor.repository.DoctorScheduleRepository;
import com.clinic.doctor.service.DoctorScheduleService;
import com.clinic.mapper.ModelMapper;

@RestController
@RequestMapping("/doctorSchedule")
public class DoctorScheduleController {

	@Autowired
	private DoctorScheduleService doctorScheduleService;

	@Autowired
	private DoctorScheduleRepository doctorScheduleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{doctorId}")
	public ResponseEntity<List<DoctorScheduleRecord>> getDoctorSchedule(@PathVariable Long doctorId) {
		List<DoctorSchedule> schedule = doctorScheduleRepository.findByDoctorId(doctorId);
		return ResponseEntity.ok(schedule.stream().map(s -> modelMapper.convertToDto(s, DoctorScheduleRecord.class))
				.collect(Collectors.toList()));
	}

	@GetMapping("/available-slots")
	public ResponseEntity<List<LocalDateTime>> getAvailableSlots(@RequestParam Long doctorId,
			@RequestParam Long serviceId, @RequestParam LocalDateTime date) {
		return ResponseEntity.ok(doctorScheduleService.getAvailableSlots(doctorId, serviceId, date));
	}

	@GetMapping("/ocupied-slots")
	public ResponseEntity<List<LocalDateTime>> getOcupiedSlots(@RequestParam Long doctorId,
			@RequestParam Long serviceId, @RequestParam LocalDateTime date) {
		return ResponseEntity.ok(doctorScheduleService.getOcupiedSlots(doctorId, serviceId, date));
	}

	@PostMapping
	@PreAuthorize("hasRole('DOCTOR')")
	public ResponseEntity<DoctorScheduleRecord> addSchedule(@RequestBody DoctorScheduleRecord record) {
		DoctorScheduleRecord saved = doctorScheduleService.addSchedule(record.getDoctor().getId(),
				record.getDayOfWeek(), record.getStartTime(), record.getEndTime());
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{scheduleId}")
	@PreAuthorize("hasRole('DOCTOR')")
	public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
		doctorScheduleService.deleteSchedule(scheduleId);
		return ResponseEntity.noContent().build();
	}
}
