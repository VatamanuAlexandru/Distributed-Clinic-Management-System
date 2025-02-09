package com.clinic.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateUtil {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static String format(LocalDateTime dateTime) {
		return dateTime != null ? dateTime.format(FORMATTER) : null;
	}

	public static LocalDateTime parse(String dateTimeString) {
		return dateTimeString != null ? LocalDateTime.parse(dateTimeString, FORMATTER) : null;
	}
}
