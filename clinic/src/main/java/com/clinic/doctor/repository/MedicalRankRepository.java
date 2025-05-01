package com.clinic.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.doctor.entity.MedicalRank;

public interface MedicalRankRepository extends JpaRepository<MedicalRank, Long> {

}
