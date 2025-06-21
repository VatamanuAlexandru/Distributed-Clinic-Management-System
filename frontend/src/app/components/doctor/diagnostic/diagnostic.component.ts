import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { DiagnosisService } from '../../../services/diagnosis/diagnosis.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-diagnostic',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './diagnostic.component.html',
  styleUrl: './diagnostic.component.scss'
})
export class DiagnosticComponent implements OnInit, OnChanges {
  @Input() patientId!: string;

  diagnoses: any[] = [];
  filteredDiagnoses: any[] = [];
  selectedDiagnosis: any = null;
  showModal = false;
  modalType: 'details' | 'add' | 'edit' = 'details';
  isLoading = false;
  error: string | null = null;
  toast: { type: 'success' | 'error', message: string } | null = null;

  filter: string = '';
  severityFilter: string = '';
  treatmentFilter: string = '';
  severityOptions = ['Ușor', 'Moderată', 'Severă'];

  formData: any = {};

  constructor(private diagnosisService: DiagnosisService) { }

  ngOnInit() {
    if (this.patientId) {
      this.loadDiagnoses();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['patientId'] && changes['patientId'].currentValue) {
      this.loadDiagnoses();
    }
  }

  loadDiagnoses() {
    if (!this.patientId) return;
    this.isLoading = true;
    this.selectedDiagnosis = null;
    this.diagnosisService.getDiagnosesByPatientId(this.patientId).subscribe({
      next: data => {
        this.diagnoses = data || [];
        this.filterDiagnoses();
        this.isLoading = false;
        this.error = null;
      },
      error: () => {
        this.error = 'Eroare la încărcarea diagnosticului!';
        this.diagnoses = [];
        this.isLoading = false;
      }
    });
  }

  filterDiagnoses() {
    this.filteredDiagnoses = this.diagnoses.filter(d => {
      let match = true;
      if (this.filter) {
        match = (d.condition?.name ?? '').toLowerCase().includes(this.filter.toLowerCase())
          || (d.medicalHistory?.name ?? '').toLowerCase().includes(this.filter.toLowerCase());
      }
      if (match && this.severityFilter) {
        match = (d.severity ?? '').toLowerCase() === this.severityFilter.toLowerCase();
      }
      if (match && this.treatmentFilter) {
        match = this.treatmentFilter === 'da' ? !!d.treatment : !d.treatment;
      }
      return match;
    });
  }

  viewDetails(diagnosis: any) {
    this.selectedDiagnosis = diagnosis;
    this.modalType = 'details';
    this.showModal = true;
  }

  editDiagnosis(diagnosis: any) {
    // Shallow copy for form edit
    this.formData = {
      ...diagnosis,
      condition: { ...diagnosis.condition },
      medicalHistory: { ...diagnosis.medicalHistory }
    };
    this.modalType = 'edit';
    this.showModal = true;
  }

  openAddModal() {
    this.formData = {
      condition: { name: '' },
      diagnosisDate: '',
      medicalHistory: { name: '' },
      severity: '',
      status: ''
    };
    this.modalType = 'add';
    this.showModal = true;
  }

  saveDiagnosis() {
    if (this.modalType === 'add') {
      this.diagnosisService.addDiagnosis(this.patientId, this.formData).subscribe({
        next: () => {
          this.toast = { type: 'success', message: 'Diagnostic adăugat cu succes!' };
          setTimeout(() => this.toast = null, 2500);
          this.closeModal();
          this.loadDiagnoses();
        },
        error: () => {
          this.toast = { type: 'error', message: 'Eroare la adăugarea diagnosticului!' };
          setTimeout(() => this.toast = null, 2500);
        }
      });
    } else if (this.modalType === 'edit') {
      this.diagnosisService.updateDiagnosis(this.patientId, this.formData.id, this.formData).subscribe({
        next: () => {
          this.toast = { type: 'success', message: 'Diagnostic salvat cu succes!' };
          setTimeout(() => this.toast = null, 2500);
          this.closeModal();
          this.loadDiagnoses();
        },
        error: () => {
          this.toast = { type: 'error', message: 'Eroare la salvarea diagnosticului!' };
          setTimeout(() => this.toast = null, 2500);
        }
      });
    }
  }

  deleteDiagnosis(diag: any) {
    if (!confirm(`Sigur ștergi diagnosticul "${diag.condition?.name}"?`)) return;
    this.diagnosisService.deleteDiagnosis(this.patientId, diag.id).subscribe({
      next: () => {
        this.toast = { type: 'success', message: 'Diagnostic șters cu succes!' };
        setTimeout(() => this.toast = null, 2500);
        this.loadDiagnoses();
      },
      error: () => {
        this.toast = { type: 'error', message: 'Eroare la ștergerea diagnosticului!' };
        setTimeout(() => this.toast = null, 2500);
      }
    });
  }

  closeModal() {
    this.showModal = false;
    this.selectedDiagnosis = null;
    this.formData = {};
  }
}
