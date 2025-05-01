package com.clinic.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.doctor.entity.MedicalTitle;

public interface MedicalTitleRepository extends JpaRepository<MedicalTitle, Long> {

}
