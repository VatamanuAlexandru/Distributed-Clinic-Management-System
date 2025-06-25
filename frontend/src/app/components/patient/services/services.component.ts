import { Component, OnInit } from '@angular/core';
import { ServicesService } from '../../../services/services/services.service';
import { DepartmentService } from '../../../services/department/department.service';
import { CommonModule } from '@angular/common';
import { SelectSingleComponent } from "../../select-single/select-single.component";
import { RouterModule } from '@angular/router';

interface DepartmentOption {
  selectedId: number;
  label: string;
}

@Component({
  selector: 'app-services',
  standalone: true,
  imports: [CommonModule, SelectSingleComponent, RouterModule],
  templateUrl: './services.component.html',
  styleUrl: './services.component.scss'
})
export class ServicesComponent implements OnInit {
  services: any[] = [];
  departments: DepartmentOption[] = [];
  selectedDepartmentId: number | null = null;

  constructor(
    private servicesService: ServicesService,
    private departmentService: DepartmentService
  ) { }

  get cheapServicesCount(): number {
    return (this.services ?? []).filter(s => s.price < 100).length;
  }
  get avgDuration(): number {
    if (!this.services?.length) return 0;
    const total = this.services.reduce((acc, s) => acc + (s.durationMinutes || 0), 0);
    return Math.round(total / this.services.length);
  }

  ngOnInit(): void {
    this.loadDepartments();
    this.loadServices();
  }

  loadDepartments() {
    this.departmentService.getDepartments().subscribe(data => {
      console.log('departments response', data);
      this.departments = (data || []).map((d: any) => ({
        selectedId: d.selectedId,
        label: d.label
      }));
    });
  }
  

  loadServices(deptId?: number) {
    this.servicesService.getServices(deptId).subscribe(response => {
      this.services = response;
    });
  }

  onDepartmentSelected(deptId: number | undefined) {
    this.selectedDepartmentId = deptId ?? null;
    this.loadServices(deptId ?? undefined);
  }

  getDepartmentColor(departmentName: string): string {
    const map: { [key: string]: string } = {
      'Pediatrie': 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200',
      'Dermatologie': 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
      'Cardiologie': 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
      'Ginecologie': 'bg-pink-100 text-pink-800 dark:bg-pink-900 dark:text-pink-200',
      'ORL': 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
      'Psihologie': 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-200',
    };
    return map[departmentName] || 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200';
  }

  get selectedDepartmentLabel(): string {
    if (!this.selectedDepartmentId) return '';
    const found = this.departments.find(d => d.selectedId === this.selectedDepartmentId);
    return found?.label || '';
  }
}
