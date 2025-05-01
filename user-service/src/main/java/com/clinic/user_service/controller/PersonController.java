package com.clinic.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.common.dto.PersonRecord;
import com.clinic.user_service.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping("/{id}")
	public ResponseEntity<PersonRecord> getPersonRecordByPersonId(@PathVariable Long personId) {
		PersonRecord person = personService.getRecordByPersonId(personId);
		return ResponseEntity.ok(person);
	}
}
