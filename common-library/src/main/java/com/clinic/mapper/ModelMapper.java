package com.clinic.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.proxy.HibernateProxy;
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

	private <T, K> void mapComplexFields(T source, K target, boolean isEntity)
			throws IllegalAccessException, ClassNotFoundException {

		List<Field> complexFields = getComplexFields(source.getClass());

		for (Field field : complexFields) {
			field.setAccessible(true);
			Object value = field.get(source);
			if (value == null)
				continue;

			Field targetField;
			try {
				targetField = target.getClass().getDeclaredField(field.getName());
				if (targetField.isAnnotationPresent(ExcludeMapping.class)) {
					continue;
				}
			} catch (NoSuchFieldException e) {
				continue;
			}

			if (value instanceof Collection<?> collection && !collection.isEmpty()) {
				Object firstItem = collection.iterator().next();
				Class<?> componentClass = HibernateProxy.class.isAssignableFrom(firstItem.getClass())
						? ((HibernateProxy) firstItem).getHibernateLazyInitializer().getPersistentClass()
						: firstItem.getClass();

				String className = componentClass.getSimpleName().replace("Record", "");
				String packageName = isEntity ? target.getClass().getPackageName().replace(".dto", "")
						: componentClass.getPackageName().replace(".entity", ".dto");

				Collection<Object> mappedCollection = collection.stream().map(item -> {
					try {
						if (isEntity) {
							Class<?> entityClass = Class.forName(packageName + ".entity." + className);
							return convertToEntity(item, entityClass);
						} else {
							Class<?> dtoClass = Class.forName(packageName + "." + className + "Record");
							return convertToDto(item, dtoClass);
						}
					} catch (Exception ex) {
						throw new RuntimeException("Failed to map collection item in field: " + field.getName(), ex);
					}
				}).collect(Collectors.toList());

				targetField.setAccessible(true);
				targetField.set(target, mappedCollection);
				continue;
			}

			Class<?> realClass = HibernateProxy.class.isAssignableFrom(value.getClass())
					? ((HibernateProxy) value).getHibernateLazyInitializer().getPersistentClass()
					: value.getClass();

			String className = realClass.getSimpleName().replace("Record", "");
			String packageName = isEntity ? target.getClass().getPackageName().replace(".dto", "")
					: realClass.getPackageName().replace(".entity", ".dto");

			Object mappedValue = isEntity ? convertToEntity(value, Class.forName(packageName + ".entity." + className))
					: convertToDto(value, Class.forName(packageName + "." + className + "Record"));

			targetField.setAccessible(true);
			targetField.set(target, mappedValue);
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
