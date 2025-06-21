import { Injectable } from '@angular/core';
import { TestParameterResult } from '../../models/medical-test';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TestParameterResultService {
  private readonly baseUrl = 'http://localhost:8080/clinic/patients';

  constructor(private http: HttpClient) { }

  list(patientId: string, testId: string): Observable<TestParameterResult[]> {
    return this.http.get<TestParameterResult[]>(
      `${this.baseUrl}/${patientId}/tests/${testId}/parameters`
    );
  }

  get(patientId: string, testId: string, paramId: string): Observable<TestParameterResult> {
    return this.http.get<TestParameterResult>(
      `${this.baseUrl}/${patientId}/tests/${testId}/parameters/${paramId}`
    );
  }

  create(
    patientId: string,
    testId: string,
    param: TestParameterResult
  ): Observable<TestParameterResult> {
    return this.http.post<TestParameterResult>(
      `${this.baseUrl}/${patientId}/tests/${testId}/parameters`,
      param
    );
  }

  update(
    patientId: string,
    testId: string,
    paramId: string,
    param: TestParameterResult
  ): Observable<TestParameterResult> {
    return this.http.put<TestParameterResult>(
      `${this.baseUrl}/${patientId}/tests/${testId}/parameters/${paramId}`,
      param
    );
  }

  delete(patientId: string, testId: string, paramId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/${patientId}/tests/${testId}/parameters/${paramId}`
    );
  }
}