import { Component, OnInit } from '@angular/core';
import { MedicalHistoryService } from '../../../services/medical-history/medical-history.service';
import { AuthService } from '../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-medical-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './medical-history.component.html',
  styleUrl: './medical-history.component.scss'
})
export class MedicalHistoryComponent implements OnInit {
  history: any[] = [];
  patientId!: number;

  constructor(
    private medicalHistoryService: MedicalHistoryService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.getCurrentPatientId().subscribe({
      next: (id) => {
        this.patientId = id;
        this.loadMedicalHistory();
      },
      error: (err) => console.error("Eroare la obținerea ID-ului pacientului:", err)
    });
  }

  loadMedicalHistory(): void {
    this.medicalHistoryService.getAppointmentsByPatientId(this.patientId).subscribe({
      next: data => this.history = data,
      error: err => console.error('Eroare la încărcare istoric:', err)
    });
  }

  goToDetails(entry: any) {
    if (entry.type === 'DIAGNOSIS') {
      this.router.navigate(['/istoric/diagnostic', entry.data.id]);
    } else {
      this.router.navigate(['/istoric/test', entry.data.id]);
    }
  }

  getSymptomNames(symptoms: any[]): string {
    return symptoms?.map(s => s.name).join(', ') || '—';
  }
}

