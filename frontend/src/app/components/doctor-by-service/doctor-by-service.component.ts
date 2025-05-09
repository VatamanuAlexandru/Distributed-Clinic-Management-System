import { Component } from '@angular/core';
import { DoctorService } from '../../services/doctor/doctor.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import { NoContentComponent } from "../no-content/no-content.component";

@Component({
  selector: 'app-doctor-by-service',
  standalone: true,
  imports: [CommonModule, NoContentComponent],
  templateUrl: './doctor-by-service.component.html',
  styleUrl: './doctor-by-service.component.scss'
})
export class DoctorByServiceComponent {
  serviceId!: number;
  doctors: any[] = [];

  constructor(private route: ActivatedRoute, private doctorService: DoctorService, private location : Location) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.serviceId = Number(params.get('serviceId'));
      this.doctorService.getDoctorsByService(this.serviceId).subscribe(data => {
        this.doctors = data;
      });
    });
  }

  back(): void {
    this.location.back();
  }
}
