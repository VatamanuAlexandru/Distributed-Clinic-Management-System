package com.clinic.administrative.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.administrative.dto.SelectDepartmentRecord;
import com.clinic.administrative.entity.Department;
import com.clinic.administrative.repository.DepartmentRepository;

@RestController
@RequestMapping("department")
public class DepartmentController {

	@Autowired
	private DepartmentRepository departmentRepository;

	@GetMapping("/select")
	public ResponseEntity<List<SelectDepartmentRecord>> getDepartmentsForSelect() {
		List<Department> departments = departmentRepository.findAll();

		List<SelectDepartmentRecord> response = departments.stream()
				.map(d -> new SelectDepartmentRecord(d.getId(), d.getName())).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

}
