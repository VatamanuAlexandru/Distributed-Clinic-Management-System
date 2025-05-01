import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  private readonly baseUrl = 'http://localhost:8085/clinic/department/select';

  constructor(private http: HttpClient) {}

  getDepartments() {
    return this.http.get<any[]>(this.baseUrl);
  }
}
