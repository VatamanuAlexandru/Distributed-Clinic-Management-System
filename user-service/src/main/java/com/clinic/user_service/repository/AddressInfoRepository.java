package com.clinic.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.user_service.entity.AddressInfo;

public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {

}
