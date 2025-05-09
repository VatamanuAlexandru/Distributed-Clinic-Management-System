import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {
  private readonly baseUrl = 'http://localhost:8080/clinic/services';

  constructor(private http: HttpClient) { }

  getServices(departmentId?: number) {
    const params = departmentId ? { params: { departmentId } } : {};
    return this.http.get<any[]>(`http://localhost:8080/clinic/services`, params);
  }
  
}
