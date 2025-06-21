import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DoctorService } from '../../../services/doctor/doctor.service';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-doctor-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './doctor-profile.component.html',
  styleUrl: './doctor-profile.component.scss'
})
export class DoctorProfileComponent implements OnInit {
  doctor: {
    id: number;
    person: any;
    medicalTitle?: any;
    medicalRank?: any;
    yearsOfExperience?: number;
  } | null = null;
  avatarUrl: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private doctorService: DoctorService,
    private location : Location
  ) { }

  ngOnInit(): void {
    const idParam = this.activatedRoute.snapshot.paramMap.get('id');
    if (idParam !== null) {
      const id = Number(idParam);
      this.doctorService.getDoctorById(id).subscribe({
        next: (doctor) => {
          this.doctor = doctor;
        },
        error: (err) => {
          console.error('Eroare la obținerea doctorului:', err);
        }
      });
    } else {
      console.error('Lipsă ID în URL');
    }
  }

  back(): void {
    this.location.back();
  }




}
