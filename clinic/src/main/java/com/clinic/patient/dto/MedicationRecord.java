package com.clinic.patient.dto;

import java.io.Serializable;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MedicationRecord extends BaseRecord{
    private static final long serialVersionUID = 1L;
    private String name;
    private String dosage;
    private String frequency;
    private String administrationRoute;
    private String sideEffects;
    private TreatmentRecord treatment;
}
