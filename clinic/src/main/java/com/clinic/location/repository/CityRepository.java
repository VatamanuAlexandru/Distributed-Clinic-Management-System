package com.clinic.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.location.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {

}
