import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { AuthService } from '../../../services/auth/auth.service';
import { DoctorService } from '../../../services/doctor/doctor.service';
import { AnalysesComponent } from "../analyses/analyses.component";
import { DiagnosticComponent } from "../diagnostic/diagnostic.component";
import { PrescriptionsComponent } from "../prescriptions/prescriptions.component";
import { MedicalHistoryComponent } from '../medical-history/medical-history.component';

interface Person {
  firstName: string;
  lastName: string;
}

interface Appointment {
  date: string;
  reason: string;
}

interface Patient {
  id: string;
  person: Person;
  history: Appointment[];
}

interface Tab {
  id: 'history' | 'analyses' | 'prescriptions' | 'diagnostics';
  label: string;
}

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [CommonModule, FormsModule, MedicalHistoryComponent, AnalysesComponent, PrescriptionsComponent, DiagnosticComponent],
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.scss'],
})
export class PatientComponent implements OnInit, OnDestroy {
  patients: Patient[] = [];
  filtered: Patient[] = [];
  selected: Patient | null = null;
  search = '';
  activeTab: Tab['id'] = 'history';
  isLoading = true;
  error: string | null = null;
  tabs: Tab[] = [
    { id: 'history', label: 'History' },
    { id: 'analyses', label: 'Analyses' },
    { id: 'prescriptions', label: 'Prescriptions' },
    { id: 'diagnostics', label: 'Diagnostics' },
  ];

  private doctorId: number | null = null;
  private searchSubject = new BehaviorSubject<string>('');
  private destroy$ = new Subject<void>();

  constructor(private doctorService: DoctorService, private authService: AuthService) { }

  ngOnInit() {
    this.setupSearch();
    // grab and store the doctor ID, then load patients
    this.authService.getCurrentDoctorId()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: id => {
          this.doctorId = id;
          this.loadPatients();
        },
        error: () => {
          this.error = 'Failed to authenticate. Please log in again.';
          this.isLoading = false;
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private setupSearch() {
    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(query => {
        this.filtered = this.patients.filter(p =>
          `${p.person.firstName} ${p.person.lastName}`
            .toLowerCase()
            .includes(query.toLowerCase())
        );
      });
  }

  private loadPatients() {
    if (!this.doctorId) return;
    this.isLoading = true;
    this.doctorService.getCompletedPatients(this.doctorId).subscribe({
      next: data => {
        this.patients = data;
        this.filtered = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Failed to load patients. Please try again.';
        this.isLoading = false;
      },
    });
  }

  /** 
   * Called by child components when they emit (updated). 
   * Re-fetches the list and re-selects the same patient by ID.
   */
  reloadPatient() {
    if (!this.doctorId || !this.selected) {
      return;
    }
    this.isLoading = true;
    this.doctorService.getCompletedPatients(this.doctorId).subscribe({
      next: data => {
        this.patients = data;
        this.filtered = data;
        // find and re-select the patient we were on
        this.selected = this.patients.find(p => p.id === this.selected!.id) || null;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Failed to reload patient. Please try again.';
        this.isLoading = false;
      }
    });
  }

  onSearch(query: string) {
    this.searchSubject.next(query);
  }

  clearSearch() {
    this.search = '';
    this.searchSubject.next('');
  }

  select(patient: Patient) {
    this.selected = patient;
    this.activeTab = 'history';
  }

  setTab(tab: Tab['id']) {
    this.activeTab = tab;
  }

  trackById(_: number, patient: Patient): string {
    return patient.id;
  }

  trackByApptDate(_: number, appt: Appointment): string {
    return appt.date;
  }
}
