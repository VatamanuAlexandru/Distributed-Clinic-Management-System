import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/user-service/api/auth';

  constructor(private http: HttpClient) { }

  getCurrentPatientId() {
    return this.http.get<number>(`${this.baseUrl}/currentPatientId`);
  }

  getCurrentDoctorId() {
    return this.http.get<number>(`${this.baseUrl}/currentDoctorId`);
  }
}
