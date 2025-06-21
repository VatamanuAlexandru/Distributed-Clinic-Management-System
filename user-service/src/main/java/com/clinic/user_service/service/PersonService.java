package com.clinic.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.dto.AddressInfoRecord;
import com.clinic.common.dto.ContactInfoRecord;
import com.clinic.common.dto.PersonRecord;
import com.clinic.common.service.BaseService;
import com.clinic.mapper.ModelMapper;
import com.clinic.user_service.entity.AddressInfo;
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
		PersonRecord personRecord = modelMapper.convertToDto(person, PersonRecord.class);

		if (person.getContactInfo() != null) {
			ContactInfoRecord contanctInfoRecord = new ContactInfoRecord();
			contanctInfoRecord.setEmailAddress(person.getContactInfo().getEmailAddress());
			contanctInfoRecord.setPhoneNumber(person.getContactInfo().getPhoneNumber());
			personRecord.setContactInfo(contanctInfoRecord);
		}

		if (person.getAddressInfo() != null) {
			AddressInfoRecord addressInfoRecord = new AddressInfoRecord();
			AddressInfo serverAddressInfo = person.getAddressInfo();
			addressInfoRecord.setBuilding(serverAddressInfo.getBuilding());
			addressInfoRecord.setCity(serverAddressInfo.getCity());
			addressInfoRecord.setCountry(serverAddressInfo.getCountry());
			addressInfoRecord.setCounty(serverAddressInfo.getCounty());
			addressInfoRecord.setEntrance(serverAddressInfo.getEntrance());
			addressInfoRecord.setFlatNumer(serverAddressInfo.getFlatNumer());
			addressInfoRecord.setPostalCode(serverAddressInfo.getPostalCode());
			addressInfoRecord.setStreetName(serverAddressInfo.getStreetName());
			addressInfoRecord.setStreetNumber(serverAddressInfo.getStreetNumber());
			personRecord.setAddressInfo(addressInfoRecord);
		}

		return personRecord;
	}

}
