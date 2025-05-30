package com.clinic.mapper;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long selectedId;
	private String label;
}
