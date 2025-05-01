package com.clinic.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.user_service.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
