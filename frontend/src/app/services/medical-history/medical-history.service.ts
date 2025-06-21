import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MedicalHistoryService {
  private baseUrl = 'http://localhost:8080/clinic/medical-history';

  constructor(private http: HttpClient) { }

  getAppointmentsByPatientId(patientId: number) {
    return this.http.get<any[]>(`${this.baseUrl}/${patientId}`);
  }

}
