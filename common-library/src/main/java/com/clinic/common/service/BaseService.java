package com.clinic.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.common.ErasablePersistableEntity;
import com.clinic.common.PersistableEntity;
import com.clinic.common.exception.EntityNotFoundException;

public abstract class BaseService<T extends PersistableEntity> {

	protected abstract JpaRepository<T, Long> getRepository();

	public List<T> findAll() {
		return getRepository().findAll();
	}

	public Optional<T> findById(Long id) {
		Optional<T> entity = getRepository().findById(id);
		if (entity.isEmpty()) {
			throw new EntityNotFoundException("Entity not found with id: " + id);
		}
		return entity;
	}

	public T save(T entity) {
		return getRepository().save(entity);
	}

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
