package com.clinic.administrative.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.administrative.entity.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

}
