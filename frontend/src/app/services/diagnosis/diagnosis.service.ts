import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiagnosisService {
  private baseUrl = 'http://localhost:8080/clinic/diagnosis';

  constructor(private http: HttpClient) { }

  getDiagnosesByPatientId(patientId: string): Observable<any> {
    return this.http.get<any[]>(`${this.baseUrl}/patient/${patientId}`);
  }

  addDiagnosis(patientId: string, data: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/patient/${patientId}`, data);
  }
  updateDiagnosis(patientId: string, diagnosisId: string, data: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/patient/${patientId}/${diagnosisId}`, data);
  }
  deleteDiagnosis(patientId: string, diagnosisId: string): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/patient/${patientId}/${diagnosisId}`);
  }

}
