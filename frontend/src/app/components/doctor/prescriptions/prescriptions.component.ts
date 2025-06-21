import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnInit,
  OnChanges,
  SimpleChanges,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { TreatmentService } from '../../../services/treatment/treatment.service';
import { MedicationService } from '../../../services/medication/medication.service';
import { Medication, Treatment } from '../../../models/treatment';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-prescriptions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './prescriptions.component.html',
  styleUrls: ['./prescriptions.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PrescriptionsComponent implements OnInit, OnChanges {
  @Input() patientId!: string;
  @Output() updated = new EventEmitter<void>();

  treatments: Treatment[] = [];
  selectedTreatment: Treatment | null = null;
  isLoading = false;
  error: string | null = null;

  // Modal tratament
  showTreatModal = false;
  treatEditData: Treatment | null = null;
  treatForm: FormGroup;

  // Medicație
  medications: Medication[] = [];
  expandedMedication: number | null = null; // indexul cardului expandat
  editMedForms: FormGroup[] = [];

  constructor(
    private treatmentService: TreatmentService,
    private medicationService: MedicationService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    // Treatment form
    this.treatForm = this.fb.group({
      treatmentType: ['', Validators.required],
      details: [''],
      duration: [''],
    });
  }

  ngOnInit(): void {
    if (this.patientId) this.loadTreatments();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['patientId']?.currentValue) this.loadTreatments();
  }

  // --- TREATMENTS ---

  loadTreatments(): void {
    this.isLoading = true;
    this.treatmentService.getTreatmentsByPatientId(this.patientId).subscribe({
      next: (data) => {
        this.treatments = data || [];
        this.selectedTreatment = null;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Eroare la încărcarea tratamentelor';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  openTreatModal(treat?: Treatment) {
    this.treatForm.reset();
    if (treat) {
      this.treatEditData = treat;
      this.treatForm.patchValue(treat);
    } else {
      this.treatEditData = null;
    }
    this.showTreatModal = true;
  }

  closeTreatModal() {
    this.showTreatModal = false;
    this.treatEditData = null;
    this.treatForm.reset();
  }

  saveTreatment() {
    if (this.treatForm.invalid) return;
    const data = this.treatForm.value;
    this.isLoading = true;
    if (this.treatEditData) {
      this.treatmentService.updateTreatment(this.treatEditData.id, data).subscribe({
        next: () => {
          this.loadTreatments();
          this.closeTreatModal();
        },
        error: () => {
          this.error = 'Eroare la actualizare tratament';
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.treatmentService.addTreatment(this.patientId, data).subscribe({
        next: () => {
          this.loadTreatments();
          this.closeTreatModal();
        },
        error: () => {
          this.error = 'Eroare la adăugare tratament';
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  deleteTreatment(id: string) {
    this.isLoading = true;
    this.treatmentService.deleteTreatment(id).subscribe({
      next: () => this.loadTreatments(),
      error: () => {
        this.error = 'Eroare la ștergere tratament';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  selectTreatment(t: Treatment) {
    this.selectedTreatment = t;
    this.loadMedications();
    this.cdr.detectChanges();
  }

  deselectTreatment() {
    this.selectedTreatment = null;
    this.medications = [];
    this.expandedMedication = null;
    this.editMedForms = [];
  }

  // --- MEDICATIONS ---

  loadMedications() {
    if (!this.selectedTreatment) return;
    this.isLoading = true;
    this.medicationService.getMedicationsByTreatment(this.selectedTreatment.id).subscribe({
      next: (data) => {
        this.medications = data || [];
        // recreează formularele pentru fiecare medicament
        this.editMedForms = this.medications.map(m => this.fb.group({
          name: [m.name, Validators.required],
          dosage: [m.dosage, Validators.required],
          frequency: [m.frequency, Validators.required],
          administrationRoute: [m.administrationRoute, Validators.required],
          sideEffects: [m.sideEffects],
        }));
        this.expandedMedication = null;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Eroare la încărcarea medicamentelor';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  addExpandMedication() {
    if (!this.selectedTreatment) return;
    // medicament gol
    const newMed: Medication = {
      id: '',
      name: '',
      dosage: '',
      frequency: '',
      administrationRoute: '',
      sideEffects: ''
    };
    this.medications = [...this.medications, newMed];
    this.editMedForms = [...this.editMedForms, this.fb.group({
      name: ['', Validators.required],
      dosage: ['', Validators.required],
      frequency: ['', Validators.required],
      administrationRoute: ['', Validators.required],
      sideEffects: ['']
    })];
    this.expandedMedication = this.medications.length - 1;
    this.cdr.detectChanges();
  }

  toggleExpandMedication(idx: number) {
    this.expandedMedication = this.expandedMedication === idx ? null : idx;
    this.cdr.detectChanges();
  }

  saveMedication(idx: number) {
    if (!this.selectedTreatment) return;
    const form = this.editMedForms[idx];
    if (form.invalid) return;
    const data = form.value;
    this.isLoading = true;

    // Adăugare (dacă nu există id) sau editare
    if (!this.medications[idx].id) {
      // ADD
      this.medicationService.addMedication(this.selectedTreatment.id, data).subscribe({
        next: () => {
          this.loadMedications();
          this.expandedMedication = null;
        },
        error: () => {
          this.error = 'Eroare la adăugare medicament';
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      // EDIT
      this.medicationService.updateMedication(this.medications[idx].id, data).subscribe({
        next: () => {
          this.loadMedications();
          this.expandedMedication = null;
        },
        error: () => {
          this.error = 'Eroare la actualizare medicament';
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  cancelEditMedication(idx: number) {
    if (!this.medications[idx].id) {
      // Dacă nu are id, este nou: îl scoatem din listă!
      this.medications.splice(idx, 1);
      this.editMedForms.splice(idx, 1);
      this.expandedMedication = null;
    } else {
      // Resetăm form cu valorile originale
      const m = this.medications[idx];
      this.editMedForms[idx].patchValue({
        name: m.name,
        dosage: m.dosage,
        frequency: m.frequency,
        administrationRoute: m.administrationRoute,
        sideEffects: m.sideEffects
      });
      this.expandedMedication = null;
    }
    this.cdr.detectChanges();
  }

  deleteMedication(med: Medication) {
    if (!med.id) return; // dacă nu a fost salvat, nu apelăm backend
    this.isLoading = true;
    this.medicationService.deleteMedication(med.id).subscribe({
      next: () => this.loadMedications(),
      error: () => {
        this.error = 'Eroare la ștergere medicament';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // Utility
  trackById(_index: number, el: { id: string }) {
    return el.id || _index; // fallback la index pentru nou adăugat
  }
}
