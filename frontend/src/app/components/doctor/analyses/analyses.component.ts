import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { MedicalTest, TestParameterResult, TestCategory, TestCategoryLabels } from '../../../models/medical-test';
import { MedicalTestService } from '../../../services/medical-test/medical-test.service';
import { TestParameterResultService } from '../../../services/test-parameter-result/test-parameter-result.service';
import { TableComponent } from '../../table/table.component';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import { map, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-analyses',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, TableComponent, NgxPaginationModule],
  templateUrl: './analyses.component.html',
  styleUrls: ['./analyses.component.scss']
})
export class AnalysesComponent implements OnInit {
  @Input() patientId!: string;
  @Output() updated = new EventEmitter<void>();
  @ViewChild('pdfContent') pdfContent!: ElementRef;

  tests: MedicalTest[] = [];
  filteredTests$ = new BehaviorSubject<MedicalTest[]>([]);
  filter = '';
  sortKey = 'testDate';
  sortOrder: 'asc' | 'desc' = 'desc';
  page = 1;
  itemsPerPage = 10;

  testColumns = [
    { key: 'testName', label: 'Test Name' },
    { key: 'category', label: 'Category' },
    { key: 'testDate', label: 'Date', type: 'date', format: 'short' }
  ];


  selectedTest: MedicalTest | null = null;
  testForm!: FormGroup;
  editingTest: MedicalTest | null = null;
  showTestModal = false;

  paramForm!: FormGroup;
  editingParam: TestParameterResult | null = null;
  showParamModal = false;

  paramColumns = [
    { key: 'parameterName', label: 'Parametru' },
    { key: 'value', label: 'Valoare' },
    { key: 'unit', label: 'Unitate' },
    { key: 'referenceRange', label: 'Interval de referință' },
    { key: 'notes', label: 'Note' }
  ];

  isLoading = false;
  error: string | null = null;

  private filterSubject = new BehaviorSubject<string>('');
  private sortSubject = new BehaviorSubject<{ key: string; order: 'asc' | 'desc' }>({ key: 'testDate', order: 'desc' });

  testCategoryOptions = Object.values(TestCategory).map((value) => ({
    value: value as TestCategory,
    label: TestCategoryLabels[value as TestCategory]
  }));


  constructor(
    private testSvc: MedicalTestService,
    private paramSvc: TestParameterResultService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.initForms();
    this.setupFilterAndSort();
    this.loadTests();
  }

  initForms() {
    this.testForm = this.fb.group({
      testName: ['', Validators.required],
      category: [TestCategory.BLOOD_TEST, Validators.required],
      testDate: ['', Validators.required],
      result: ['']
    });

    this.paramForm = this.fb.group({
      parameterName: ['', Validators.required],
      value: ['', Validators.required],
      unit: ['', Validators.required],
      referenceRange: ['', Validators.required],
      notes: ['']
    });
  }

  setupFilterAndSort() {
    combineLatest([
      this.filterSubject.pipe(debounceTime(300), distinctUntilChanged()),
      this.sortSubject
    ]).pipe(
      map(([filter, sort]) => ({ filter, sort })),
      switchMap(({ filter, sort }) => this.applyFilterAndSort(filter, sort))
    ).subscribe(filteredTests => this.filteredTests$.next(filteredTests));
  }

  loadTests() {
    this.isLoading = true;
    this.testSvc.list(this.patientId).subscribe({
      next: list => {
        this.tests = (list || []).map(test => ({
          ...test,
          parameterResults: test.parameterResults ?? []
        }));
        this.applyFilterAndSort(this.filter, { key: this.sortKey, order: this.sortOrder });
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Eroare la încărcarea testelor.';
        this.isLoading = false;
      }
    });
  }

  applyFilterAndSort(filter: string, sort: { key: string; order: 'asc' | 'desc' }): Observable<MedicalTest[]> {
    return new Observable(observer => {
      let filtered = this.tests.filter(t =>
        t.testName.toLowerCase().includes(filter.toLowerCase()) ||
        t.category.toString().toLowerCase().includes(filter.toLowerCase())
      );

      filtered.sort((a, b) => {
        const valA = a[sort.key as keyof MedicalTest];
        const valB = b[sort.key as keyof MedicalTest];
        const order = sort.order === 'asc' ? 1 : -1;
        if (sort.key === 'category') {
          return (valA?.toString() ?? '').localeCompare(valB?.toString() ?? '') * order;
        }
        if (typeof valA === 'string' && typeof valB === 'string') {
          return valA.localeCompare(valB) * order;
        }
        if (valA === undefined || valB === undefined) {
          return valA === undefined ? (valB === undefined ? 0 : -1) : 1;
        }
        return (valA > valB ? 1 : -1) * order;
      });

      observer.next(filtered || []);
      observer.complete();
    });
  }

  onFilterChange() {
    this.filterSubject.next(this.filter);
  }

  onSortChange(key: string) {
    this.sortOrder = this.sortKey === key && this.sortOrder === 'asc' ? 'desc' : 'asc';
    this.sortKey = key;
    this.sortSubject.next({ key, order: this.sortOrder });
  }

  onPageChange(page: number) {
    this.page = page;
  }

  openTestModal(test?: MedicalTest) {
    this.editingTest = test || null;
    this.testForm.reset(test || {
      testName: '',
      category: TestCategory.BLOOD_TEST,
      testDate: '',
      result: ''
    });
    this.showTestModal = true;
  }

  saveTest() {
    if (this.testForm.invalid) return;
    let data = this.testForm.value as MedicalTest;

    // Adaugă ora dacă lipsește (patch pt. LocalDateTime)
    if (data.testDate && data.testDate.length === 10) {
      data.testDate = data.testDate + 'T00:00:00';
    }

    const operation = this.editingTest
      ? this.testSvc.update(this.patientId, this.editingTest.id!, { ...data, id: this.editingTest.id })
      : this.testSvc.create(this.patientId, data);

    operation.subscribe({
      next: () => {
        this.loadTests();
        this.updated.emit();
        this.showTestModal = false;
      },
      error: () => this.error = 'Eroare la salvarea testului.'
    });
  }

  deleteTest(test: MedicalTest) {
    this.testSvc.delete(this.patientId, test.id!).subscribe({
      next: () => {
        this.loadTests();
        this.updated.emit();
      },
      error: () => this.error = 'Eroare la ștergerea testului.'
    });
  }

  viewDetails(test: MedicalTest) {
    this.selectedTest = { ...test, parameterResults: [] }; // asigură-te că nu rămân vechi

    // Încarcă proaspat parametrii de pe server (safe și dacă sunt null/undefined)
    this.paramSvc.list(this.patientId, test.id!).subscribe({
      next: params => {
        // dacă se încarcă, actualizează selectedTest cu lista nouă
        if (this.selectedTest) {
          this.selectedTest.parameterResults = params ?? [];
        }
      },
      error: () => this.error = 'Eroare la încărcarea parametrilor.'
    });
  }


  backToList() {
    this.selectedTest = null;
  }

  openParamModal(param?: TestParameterResult) {
    this.editingParam = param || null;
    this.paramForm.reset(param || {
      parameterName: '', value: '', unit: '', referenceRange: '', notes: ''
    });
    this.showParamModal = true;
  }

  saveParam() {
    if (!this.selectedTest || this.paramForm.invalid) return;
    const data = this.paramForm.value as TestParameterResult;
    const operation = this.editingParam
      ? this.paramSvc.update(this.patientId, this.selectedTest.id!, this.editingParam.id!, { ...data, id: this.editingParam.id })
      : this.paramSvc.create(this.patientId, this.selectedTest.id!, data);

    operation.subscribe({
      next: updated => this.onParamSaved(updated),
      error: () => this.error = 'Eroare la salvarea parametrului.'
    });
  }

  onParamSaved(_: TestParameterResult) {
    this.paramSvc.list(this.patientId, this.selectedTest!.id!).subscribe({
      next: params => {
        this.selectedTest = { ...this.selectedTest!, parameterResults: params ?? [] };
        this.updated.emit();
        this.showParamModal = false;
      },
      error: () => this.error = 'Eroare la încărcarea parametrilor.'
    });
  }

  deleteParam(param: TestParameterResult) {
    if (!this.selectedTest || !confirm(`Ștergi parametrul '${param.parameterName}'?`)) return;
    this.paramSvc.delete(this.patientId, this.selectedTest.id!, param.id!).subscribe({
      next: () => this.onParamSaved(param),
      error: () => this.error = 'Eroare la ștergerea parametrului.'
    });
  }

  onParamAction(event: { type: string; row: TestParameterResult }) {
    if (event.type === 'edit') this.openParamModal(event.row);
    else if (event.type === 'delete') this.deleteParam(event.row);
  }

  async exportPdf() {
    try {
      this.isLoading = true;
      const el = this.pdfContent.nativeElement;
      const canvas = await html2canvas(el, { scale: 2 });
      const img = canvas.toDataURL('image/png');
      const pdf = new jsPDF({ orientation: 'portrait' });
      const w = pdf.internal.pageSize.getWidth() - 20;
      const h = (canvas.height * w) / canvas.width;
      pdf.addImage(img, 'PNG', 10, 10, w, h);
      pdf.save(`parametri_${this.selectedTest?.testName || 'test'}.pdf`);
    } catch {
      this.error = 'Eroare la exportul PDF.';
    } finally {
      this.isLoading = false;
    }
  }

  onTestAction(event: { type: string; row: MedicalTest }) {
    switch (event.type) {
      case 'view':
        this.viewDetails(event.row);
        break;
      case 'edit':
        this.openTestModal(event.row);
        break;
      case 'delete':
        this.deleteTest(event.row);
        break;
    }
  }

}