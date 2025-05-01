package com.clinic.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

	/**
	 * Converts an entity to a DTO, excluding fields annotated with @ExcludeMapping
	 * 
	 * @param <T> entity -> The entity to be converted
	 * @param <K> dtoClass -> The DTO class type
	 * @return DTO object;
	 */
	public <T, K> K convertToDto(T entity, Class<K> dtoClass) {
		if (entity == null) {
			return null;
		}
		try {
			K dto = dtoClass.getDeclaredConstructor().newInstance();
			List<String> excludedFields = getExcludedFields(dto.getClass());
			BeanUtils.copyProperties(entity, dto, excludedFields.toArray(new String[0]));
			mapComplexFields(entity, dto, false);
			return dto;
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert Entity to DTO", e);
		}
	}

	/**
	 * Converts an DTO to a entity
	 * 
	 * @param <K> dto -> The dto to be converted
	 * @param <T> entityClass -> The entity class type
	 * @return entity object;
	 */
	public <T, K> T convertToEntity(K dto, Class<T> entityClass) {
		if (dto == null) {
			return null;
		}
		try {
			T entity = entityClass.getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(dto, entity);
			mapComplexFields(dto, entity, true);
			return entity;
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert DTO to Entity", e);
		}
	}

	/**
	 * Maps complex fields between source and target objects recursively. Handles
	 * both Entity to DTO and DTO to Entity conversions.
	 * 
	 * @param source   The source object (entity or DTO).
	 * @param target   The target object (entity or DTO).
	 * @param isEntity Boolean flag indicating if target is an entity.
	 * @throws IllegalAccessException if the field is inaccessible.
	 * @throws ClassNotFoundException if target class is not found.
	 */
	private <T, K> void mapComplexFields(T source, K target, boolean isEntity)
			throws IllegalAccessException, ClassNotFoundException {
		List<Field> complexFields = getComplexFields(source.getClass());
		for (Field field : complexFields) {
			field.setAccessible(true);
			Object value = field.get(source);
			String className = value.getClass().getSimpleName().replace("Record", "");
			String packageName = target.getClass().getPackageName().replace(".entity", "");
			if (value != null && isEntity == true) {
				Class<?> targetClass = Class.forName(packageName + ".entity." + className);
				Object mappedValue = convertToEntity(value, targetClass);
				try {
					Field targetField = target.getClass().getDeclaredField(field.getName());
					targetField.setAccessible(true);
					targetField.set(target, mappedValue);
				} catch (NoSuchFieldException e) {
				}
			} else if (value != null) {
				Object mappedValue = convertToDto(value, Class.forName(packageName + "." + className + "Record"));
				try {
					Field targetField = target.getClass().getDeclaredField(field.getName());
					targetField.setAccessible(true);
					targetField.set(target, mappedValue);
				} catch (NoSuchFieldException e) {

				}
			}
		}
	}

	private List<String> getExcludedFields(Class<?> clazz) {
		return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ExcludeMapping.class))
				.map(Field::getName).collect(Collectors.toList());
	}

	private List<Field> getComplexFields(Class<?> clazz) {
		return Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> !field.getType().isPrimitive() && !field.getType().getName().startsWith("java."))
				.collect(Collectors.toList());
	}
}
