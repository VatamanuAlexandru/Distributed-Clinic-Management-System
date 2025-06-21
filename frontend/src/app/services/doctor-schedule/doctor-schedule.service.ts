import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


export interface DoctorSchedule {
  id: number;
  doctor: { id: number };
  dayOfWeek: string;
  startTime: string; // ex: "09:00"
  endTime: string;   // ex: "17:00"
}

@Injectable({
  providedIn: 'root'
})
export class DoctorScheduleService {
  private baseUrl = 'http://localhost:8080/clinic/doctorSchedule';

  constructor(private http: HttpClient) { }

  getAvailableSlots(doctorId: number, serviceId: number, date: string): Observable<string[]> {
    const params = new HttpParams()
      .set('doctorId', doctorId)
      .set('serviceId', serviceId)
      .set('date', date);

    return this.http.get<string[]>(`${this.baseUrl}/available-slots`, { params });
  }

  getOcupiedSlots(doctorId: number, serviceId: number, date: string): Observable<string[]> {
    const params = new HttpParams()
      .set('doctorId', doctorId)
      .set('serviceId', serviceId)
      .set('date', date);

    return this.http.get<string[]>(`${this.baseUrl}/ocupied-slots`, { params });
  }

  getDoctorSchedule(doctorId: number): Observable<DoctorSchedule[]> {
    return this.http.get<DoctorSchedule[]>(`${this.baseUrl}/${doctorId}`);
  }

  addSchedule(schedule: Partial<DoctorSchedule>): Observable<DoctorSchedule> {
    return this.http.post<DoctorSchedule>(this.baseUrl, schedule);
  }

  deleteSchedule(scheduleId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${scheduleId}`);
  }


}
