package com.clinic.common.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

@Transactional
public interface BaseService<T> {
	Optional<T> findById(Long id);

	List<T> findAll();

	T save(T entity);

	void delete(Long id);

}
