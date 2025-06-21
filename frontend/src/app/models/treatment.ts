export interface Treatment {
  id: string;
  treatmentType: string;
  details: string;
  duration?: string;
  medications?: Medication[];
}

export interface Medication {
  id: string;
  name: string;
  dosage: string;
  frequency: string;
  administrationRoute: string;
  sideEffects?: string;
}