import { Component, OnInit } from '@angular/core';
import { SelectSingleComponent } from "../select-single/select-single.component";
import { DepartmentService } from '../../services/department/department.service';

@Component({
  selector: 'app-doctor',
  standalone: true,
  imports: [SelectSingleComponent],
  templateUrl: './doctor.component.html',
  styleUrl: './doctor.component.scss'
})
export class DoctorComponent implements OnInit{
  departments : any;  
  selectedDepartmentId: number | null = null;

  constructor(private departmentService: DepartmentService){

  }

  ngOnInit(): void {
    this.departmentService.getDepartments().subscribe(data => {
      this.departments = data;
    });
  }

  onDepartmentChange(event: Event) {
    this.selectedDepartmentId = +(event.target as HTMLSelectElement).value;
  }
}
