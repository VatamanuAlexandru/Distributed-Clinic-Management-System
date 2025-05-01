package com.clinic.administrative.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.administrative.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
