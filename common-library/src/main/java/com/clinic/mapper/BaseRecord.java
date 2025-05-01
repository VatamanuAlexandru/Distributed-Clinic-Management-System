package com.clinic.mapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private int version;
	
	

}
