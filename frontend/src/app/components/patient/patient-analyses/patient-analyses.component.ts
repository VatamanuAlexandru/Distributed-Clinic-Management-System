import { Component, OnInit } from '@angular/core';
import { MedicalTestService } from '../../../services/medical-test/medical-test.service';
import { AuthService } from '../../../services/auth/auth.service';
import { MedicalTest } from '../../../models/medical-test';
import { TableComponent } from '../../table/table.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-patient-analyses',
  standalone: true,
  imports: [CommonModule, TableComponent, FormsModule],
  templateUrl: './patient-analyses.component.html',
  styleUrls: ['./patient-analyses.component.scss']
})
export class PatientAnalysesComponent implements OnInit {
  tests: MedicalTest[] = [];
  filtered: MedicalTest[] = [];
  isLoading = false;
  error: string | null = null;

  columns = [
    { key: 'testName', label: 'Denumire analiză' },
    { key: 'category', label: 'Categorie' },
    { key: 'testDate', label: 'Data' },
    { key: 'result', label: 'Rezultat' }
  ];

  sortKey: string = 'testDate';
  sortDir: 'asc' | 'desc' = 'desc';
  filterText: string = '';

  constructor(
    private testService: MedicalTestService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.isLoading = true;
    this.authService.getCurrentPatientId().subscribe({
      next: (id) => {
        this.testService.list(id.toString()).subscribe({
          next: tests => {
            this.tests = tests || [];
            this.applyFilterSort();
            this.isLoading = false;
          },
          error: () => {
            this.error = 'Eroare la încărcarea analizelor!';
            this.isLoading = false;
          }
        });
      },
      error: () => {
        this.error = 'Eroare la identificarea pacientului!';
        this.isLoading = false;
      }
    });
  }

  onSort(event: { key: string, direction: 'asc' | 'desc' }) {
    this.sortKey = event.key;
    this.sortDir = event.direction;
    this.applyFilterSort();
  }

  onFilterChange() {
    this.applyFilterSort();
  }

  applyFilterSort() {
    let result = this.tests;
    if (this.filterText) {
      const ft = this.filterText.toLowerCase();
      result = result.filter(t =>
        t.testName?.toLowerCase().includes(ft) ||
        t.category?.toLowerCase().includes(ft) ||
        t.result?.toLowerCase().includes(ft)
      );
    }
    result = result.slice().sort((a: any, b: any) => {
      let aVal = a[this.sortKey];
      let bVal = b[this.sortKey];
      if (this.sortKey === 'testDate') {
        aVal = new Date(aVal);
        bVal = new Date(bVal);
      }
      if (aVal === bVal) return 0;
      if (this.sortDir === 'asc') return aVal > bVal ? 1 : -1;
      return aVal < bVal ? 1 : -1;
    });
    this.filtered = result;
  }

  exportCSV() {
    if (!this.filtered.length) return;
    const headers = this.columns.map(c => `"${c.label}"`).join(',');
    const rows = this.filtered.map(row =>
      this.columns.map(col => `"${(row[col.key as keyof MedicalTest] || '').toString().replace(/"/g, '""')}"`).join(',')
    );
    const csv = [headers, ...rows].join('\r\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'analize_medicale.csv';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  }

  rowClassFunction(row: any): string {
    return '';
  }
}
