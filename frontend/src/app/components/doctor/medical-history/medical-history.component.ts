import { Component, Input, OnInit } from '@angular/core';
import { MedicalHistoryService } from '../../../services/medical-history/medical-history.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-medical-history',
  standalone: true,
  templateUrl: './medical-history.component.html',
  styleUrls: ['./medical-history.component.scss'],
  imports: [CommonModule],
})
export class MedicalHistoryComponent implements OnInit {
  @Input() patientId!: string;

  entries: any[] = [];
  selected: any | null = null;
  isLoading = false;
  error: string | null = null;

  constructor(private svc: MedicalHistoryService) {}

  ngOnInit() {
    if (this.patientId) {
      this.loadEntries();
    }
  }

  ngOnChanges() {
    this.loadEntries();
  }

  loadEntries() {
    if (!this.patientId) return;
    this.isLoading = true;
    this.error = null;
    this.svc.getAppointmentsByPatientId(Number(this.patientId)).subscribe({
      next: (data) => {
        this.entries = data || [];
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Eroare la încărcarea istoricului.';
        this.isLoading = false;
      }
    });
  }

  viewDetails(entry: any) {
    this.selected = entry;
  }

  backToList() {
    this.selected = null;
  }

  // (Optional) Format type in template
  typeLabel(type: string) {
    if (type === 'TEST') return 'Analiză';
    if (type === 'DIAGNOSIS') return 'Diagnostic';
    return type;
  }
}
