package com.clinic.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.location.entity.County;

public interface CountyRepository extends JpaRepository<County, Long> {

}
