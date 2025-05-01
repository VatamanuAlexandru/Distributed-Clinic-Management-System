package com.clinic.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.location.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
