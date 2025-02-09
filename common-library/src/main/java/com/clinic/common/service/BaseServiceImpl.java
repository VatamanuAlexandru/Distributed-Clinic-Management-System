package com.clinic.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.common.ErasablePersistableEntity;
import com.clinic.common.exception.EntityNotFoundException;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected abstract JpaRepository<T, Long> getRepository();

	@Override
	public List<T> findAll() {
		return getRepository().findAll();
	}

	@Override
	public Optional<T> findById(Long id) {
		Optional<T> entity = getRepository().findById(id);
		if (entity.isEmpty()) {
			throw new EntityNotFoundException("Entity not found with id: " + id);
		}
		return entity;
	}

	@Override
	public T save(T entity) {
		return getRepository().save(entity);
	}

	@Override
	public void delete(Long id) {
		Optional<T> entity = getRepository().findById(id);
		if (entity.isEmpty()) {
			throw new EntityNotFoundException("Entity nod found with id " + id);
		}
		T foundEntity = entity.get();
		if (foundEntity instanceof ErasablePersistableEntity erasablePersistableEntity) {
			erasablePersistableEntity.markAsDeleted();
			getRepository().save(foundEntity);
		} else {
			getRepository().deleteById(id);
		}
	}
}
