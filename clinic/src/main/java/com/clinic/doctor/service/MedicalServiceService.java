package com.clinic.doctor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.doctor.dto.MedicalServiceRecord;
import com.clinic.doctor.entity.MedicalService;
import com.clinic.doctor.repository.MedicalServiceRepository;
import com.clinic.mapper.ModelMapper;

@Service
public class MedicalServiceService extends BaseService<MedicalService> {

	@Autowired
	private MedicalServiceRepository medicalServiceRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	protected JpaRepository<MedicalService, Long> getRepository() {
		return medicalServiceRepository;
	}

	public List<MedicalServiceRecord> getAllServices() {
		List<MedicalService> services = medicalServiceRepository.findAll();
		return services.stream().map(service -> modelMapper.convertToDto(service, MedicalServiceRecord.class))
				.collect(Collectors.toList());
	}

	public List<MedicalServiceRecord> getServicesByDepartmentId(Long departmentId) {
		List<MedicalService> services = medicalServiceRepository.findByDepartmentId(departmentId);
		return services.stream().map(service -> modelMapper.convertToDto(service, MedicalServiceRecord.class))
				.collect(Collectors.toList());
	}

}
