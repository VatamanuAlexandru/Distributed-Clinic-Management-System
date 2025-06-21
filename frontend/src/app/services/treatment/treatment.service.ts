import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TreatmentService {
  private baseUrl = 'http://localhost:8080/clinic/treatment';

  constructor(private http: HttpClient) { }

  getTreatmentsByPatientId(patientId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/patient/${patientId}`);
  }

  getTreatmentById(treatmentId: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${treatmentId}`);
  }

  addTreatment(diagnosisId: string, data: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/${diagnosisId}`, data);
  }

  updateTreatment(treatmentId: string, data: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${treatmentId}`, data);
  }

  deleteTreatment(treatmentId: string): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${treatmentId}`);
  }
}
