import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MedicalTest } from '../../models/medical-test';

@Injectable({
  providedIn: 'root'
})
export class MedicalTestService {
  private readonly baseUrl = 'http://localhost:8080/clinic/patients';

  constructor(private http: HttpClient) { }


  list(patientId: string): Observable<MedicalTest[]> {
    return this.http.get<MedicalTest[]>(`${this.baseUrl}/${patientId}/tests`);
  }

  get(patientId: string, testId: string): Observable<MedicalTest> {
    return this.http.get<MedicalTest>(`${this.baseUrl}/${patientId}/tests/${testId}`);
  }

  create(patientId: string, test: MedicalTest): Observable<MedicalTest> {
    return this.http.post<MedicalTest>(`${this.baseUrl}/${patientId}/tests`, test);
  }

  update(patientId: string, testId: string, test: MedicalTest): Observable<MedicalTest> {
    return this.http.put<MedicalTest>(`${this.baseUrl}/${patientId}/tests/${testId}`, test);
  }

  delete(patientId: string, testId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${patientId}/tests/${testId}`);
  }
}
