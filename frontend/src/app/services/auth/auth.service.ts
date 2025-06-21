import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8080/user-service/api/auth';

  constructor(private http: HttpClient, private router: Router) { }

  login(email: string, password: string) {
    return this.http.post<{ token: string, roles: string[] }>(`${this.baseUrl}/login`, { email, password });
  }

  saveRoles(roles: string[]) {
    localStorage.setItem('roles', JSON.stringify(roles));
  }

  getRoles(): string[] {
    const roles = localStorage.getItem('roles');
    return roles ? JSON.parse(roles) : [];
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  getCurrentPatientId(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/currentPatientId`);
  }

  getCurrentDoctorId(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/currentDoctorId`);
  }

}
