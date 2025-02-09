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
			List<String> excludedFields = getExcludedFields(entity.getClass());
			BeanUtils.copyProperties(entity, dto, excludedFields.toArray(new String[0]));
			return dto;
		} catch (Exception e) {
			throw new RuntimeException("Failed to convertEntity to DTO", e);
		}
	}

	public <T, K> List<K> convertToDtoList(List<T> entityList, Class<K> dtoClass) {
		return entityList.stream().map(entity -> convertToDto(entity, dtoClass)).collect(Collectors.toList());
	}

	private List<String> getExcludedFields(Class<?> clazz) {
		return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ExcludeMapping.class))
				.map(Field::getName).collect(Collectors.toList());
	}
}
