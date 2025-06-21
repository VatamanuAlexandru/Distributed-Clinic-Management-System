import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Medication } from '../../models/treatment';

@Injectable({
  providedIn: 'root'
})
export class MedicationService {
  private baseUrl = 'http://localhost:8080/clinic/medication';

  constructor(private http: HttpClient) { }

  getMedicationsByTreatment(treatmentId: string): Observable<Medication[]> {
    return this.http.get<Medication[]>(`${this.baseUrl}/treatment/${treatmentId}`);
  }

  addMedication(treatmentId: string, data: Medication): Observable<Medication> {
    return this.http.post<Medication>(`${this.baseUrl}/treatment/${treatmentId}`, data);
  }

  updateMedication(medId: string, data: Medication): Observable<Medication> {
    return this.http.put<Medication>(`${this.baseUrl}/${medId}`, data);
  }

  deleteMedication(medId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${medId}`);
  }
}
