import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DepartmentService } from '../../services/department/department.service';
import { DoctorService } from '../../services/doctor/doctor.service';
import { SelectSingleComponent } from "../select-single/select-single.component";
import { Router } from '@angular/router';

@Component({
  selector: 'app-doctor',
  standalone: true,
  imports: [SelectSingleComponent, CommonModule],
  templateUrl: './doctor.component.html',
  styleUrl: './doctor.component.scss'
})
export class DoctorComponent implements OnInit {
  departments: { selectedId: number, label: string }[] = [];
  selectedDepartmentId: number | null = null;

  doctors: {
    id: number;
    person: any,
    medicalTitle?: any;
    medicalRank?: any;
    yearsOfExperience?: number;
  }[] = [];

  constructor(private departmentService: DepartmentService, private doctorService: DoctorService, private router: Router) {

  }

  ngOnInit(): void {
    this.departmentService.getDepartments().subscribe(data => {
      this.departments = data.map((d: any) => ({
        selectedId: d.selectedId,
        label: d.label
      }));
    });
  }


  onDepartmentSelected(deptId: number | undefined) {
    if (!deptId) return;

    this.selectedDepartmentId = deptId;
    this.doctorService.getDoctorsByDepartment(deptId).subscribe(data => {
      console.log('Medici primiți:', data);
      this.doctors = data;
    });
  }

  viewProfile(id: number) {
    this.router.navigate(['/doctor', id]);
  }

}
