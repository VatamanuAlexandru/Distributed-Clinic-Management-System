package com.clinic.administrative.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.administrative.entity.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {

}
