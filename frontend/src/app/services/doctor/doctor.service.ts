import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private baseUrl = 'http://localhost:8080/clinic/doctor';

  constructor(private http: HttpClient) {}

  getDoctorsByDepartment(deptId: number) {
    return this.http.get<any[]>(`${this.baseUrl}/${deptId}`);
  }

  getDoctorById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/get/${id}`)
  }

  getDoctorsByService(id:number): Observable<any>{
    return this.http.get<any>(`${this.baseUrl}/by-service/${id}`)
  }
}
