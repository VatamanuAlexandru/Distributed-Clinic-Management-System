import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private baseUrl = 'http://localhost:8080/clinic/appointment';

  constructor(private http: HttpClient) { }

  createAppointment(data: {
    doctorId: number,
    patientId: number,
    serviceId: number,
    reason: string,
    appointmentDate: string
  }) {
    return this.http.post(`${this.baseUrl}`, data);
  }

  getByPatientId(patientId: number) {
    return this.http.get<any[]>(`${this.baseUrl}/${patientId}`);
  }

  getByDoctorId(doctorId: number) {
    return this.http.get<any[]>(`${this.baseUrl}/doctor/${doctorId}`);
  }

  updateStatus(appointmentId: number, status: string) {
    return this.http.patch(`${this.baseUrl}/${appointmentId}/status`, { status });
  }
  
}
