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
  filteredHistory: any[] = [];
  patientId!: number;
  typeFilter: '' | 'DIAGNOSIS' | 'TEST' = '';
  diagnosisCount = 0;
  testCount = 0;
  diagnosisPercent = 0;
  testPercent = 0;

  criticalKeywords = ['critic', 'anormal', 'pozitiv', 'ridicat', 'scăzut', 'urgent'];

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
      next: data => {
        this.history = data || [];
        this.calcStats();
        this.filterHistory();
      },
      error: err => console.error('Eroare la încărcare istoric:', err)
    });
  }

  filterHistory() {
    if (!this.typeFilter) {
      this.filteredHistory = this.history;
    } else {
      this.filteredHistory = this.history.filter(e => e.type === this.typeFilter);
    }
  }

  calcStats() {
    this.diagnosisCount = this.history.filter(e => e.type === 'DIAGNOSIS').length;
    this.testCount = this.history.filter(e => e.type === 'TEST').length;
    const total = this.diagnosisCount + this.testCount;
    this.diagnosisPercent = total ? Math.round(this.diagnosisCount / total * 100) : 0;
    this.testPercent = total ? 100 - this.diagnosisPercent : 0;
  }

  goToDetails(entry: any) {
    if (entry.type === 'DIAGNOSIS') {
      this.router.navigate(['/istoric/diagnostic', entry.data.id]);
    } else if (entry.type === 'TEST') {
      this.router.navigate(['/patient/analyses']);
    }
  }

  getSymptomNames(symptoms: any[]): string {
    return symptoms?.map(s => s.name).join(', ') || '—';
  }

  isCritical(result: string): boolean {
    if (!result) return false;
    return this.criticalKeywords.some(word => result.toLowerCase().includes(word));
  }
}
