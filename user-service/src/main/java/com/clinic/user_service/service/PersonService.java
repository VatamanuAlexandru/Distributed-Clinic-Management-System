package com.clinic.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.dto.PersonRecord;
import com.clinic.common.service.BaseService;
import com.clinic.mapper.ModelMapper;
import com.clinic.user_service.entity.Person;
import com.clinic.user_service.repository.PersonRepository;

@Service
public class PersonService extends BaseService<Person> {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	protected JpaRepository<Person, Long> getRepository() {
		return personRepository;
	}

	public PersonRecord getRecordByPersonId(Long personId) {
		Person person = personRepository.findById(personId)
				.orElseThrow(() -> new IllegalArgumentException("Person not found with ID: " + personId));
		return modelMapper.convertToDto(person, PersonRecord.class);
	}

}
