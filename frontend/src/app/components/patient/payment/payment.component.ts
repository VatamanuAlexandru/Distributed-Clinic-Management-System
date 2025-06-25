import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as XLSX from 'xlsx';
import { PaymentService, PaymentObligationRecord } from '../../../services/payment/payment.service';
import { AuthService } from '../../../services/auth/auth.service';
import { Subscription } from 'rxjs';
import { TableComponent } from '../../table/table.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, TableComponent, FormsModule],
  templateUrl: './payment.component.html'
})
export class PaymentComponent implements OnInit, OnDestroy {
  obligations: any[] = [];
  totalPaid = 0;
  totalDue = 0;
  paidPercent = 0;
  errorMessage: string | null = null;
  payModalOpen = false;
  obligationToPay: any = null;
  payForm = { card: '', exp: '', cvv: '' };
  obligationDetails: any = null;
  docsObligation: any = null;

  // Statistici avansate
  topObligations: any[] = [];
  lastPayment: any = null;
  unpaidCategories: { description: string, rest: number, percent: number }[] = [];
  donutLabels = ['Plătit', 'Rest'];
  donutData = [0, 0];
  donutColors = [{ backgroundColor: ['#2563eb', '#ef4444'] }];
  donutOptions = { responsive: true, cutout: '70%', plugins: { legend: { display: false } } };

  tableColumns = [
    { key: 'description', label: 'Descriere' },
    { key: 'dueDate', label: 'Scadentă', type: 'date' },
    { key: 'amount', label: 'Total', type: 'currency' },
    { key: 'paidAmount', label: 'Plătit', type: 'currency' },
    { key: 'statusLabel', label: 'Status', type: 'status' },
    { key: 'actions', label: 'Acțiuni' }
  ];

  private subscriptions: Subscription[] = [];

  constructor(
    private paymentService: PaymentService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const authSub = this.authService.getCurrentPatientId().subscribe({
      next: id => this.loadObligations(id),
      error: () => this.errorMessage = 'Eroare la autentificare.'
    });
    this.subscriptions.push(authSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadObligations(patientId: number): void {
    const obligationsSub = this.paymentService.getPatientObligations(patientId).subscribe({
      next: data => {
        this.obligations = data.map(ob => ({
          ...this.mapWithComputedFields(ob)
        }));
        this.computeStats();
        this.computeAdvancedStats();
        this.docsObligation = null;
      },
      error: () => this.errorMessage = 'Eroare la încărcarea plăților.'
    });
    this.subscriptions.push(obligationsSub);
  }

  mapWithComputedFields(ob: PaymentObligationRecord): any {
    const paidAmount = ob.documents?.filter(d => d.successful).reduce((sum, d) => sum + (d.paidAmount || 0), 0) || 0;
    const status = paidAmount >= ob.amount ? 'paid' : paidAmount > 0 ? 'partial' : 'unpaid';
    const statusLabel = status === 'paid' ? 'Plătit' : status === 'partial' ? 'Parțial' : 'Neplătit';
    return { ...ob, paidAmount, status, statusLabel };
  }

  computeStats(): void {
    const total = this.obligations.reduce((sum, ob) => sum + ob.amount, 0);
    const paid = this.obligations.reduce((sum, ob) => sum + ob.paidAmount, 0);
    this.totalPaid = paid;
    this.totalDue = Math.max(0, total - paid);
    this.paidPercent = total ? Math.min(100, Math.round((paid / total) * 100)) : 0;
  }

  onTableAction(event: { type: string; row: any }) {
    if (event.type === 'pay') {
      this.openPayModal(event.row);
    }
    if (event.type === 'details') {
      this.openObligationDetails(event.row);
    }
    if (event.type === 'docs') {
      if (this.docsObligation && this.docsObligation.id === event.row.id) {
        this.docsObligation = null;
      } else {
        this.docsObligation = event.row;
      }
    }
  }

  openPayModal(obligation: any) {
    this.payForm = { card: '', exp: '', cvv: '' };
    this.obligationToPay = obligation;
    this.payModalOpen = true;
  }

  closePayModal() {
    this.payModalOpen = false;
    this.obligationToPay = null;
  }

  submitPayment() {
    if (!this.obligationToPay) return;
    const rest = this.obligationToPay.amount - this.obligationToPay.paidAmount;
    if (rest <= 0) return;

    this.paymentService.payObligation({
      obligationId: this.obligationToPay.id,
      documentType: 'RECEIPT',
      paymentType: 'CARD',
      paidAmount: rest,
      successful: true
    }).subscribe({
      next: () => {
        this.closePayModal();
        this.authService.getCurrentPatientId().subscribe({
          next: id => this.loadObligations(id),
          error: () => this.errorMessage = 'Eroare la autentificare.'
        });
      },
      error: () => {
        this.errorMessage = 'Eroare la procesarea plății.';
        this.closePayModal();
      }
    });
  }

  openObligationDetails(obligation: any) {
    this.obligationDetails = { ...obligation };
  }

  closeObligationDetails() {
    this.obligationDetails = null;
  }

  computeAdvancedStats() {
    // Top 3 obligații
    this.topObligations = [...this.obligations].sort((a, b) => b.amount - a.amount).slice(0, 3);

    // Ultima plată
    let docs: Array<{
      id: number;
      createdAt: string;
      paidAmount: number;
      successful: boolean;
      description: string;
    }> = this.obligations.flatMap((ob: PaymentObligationRecord) =>
      (ob.documents || []).filter((d: { successful: boolean }) => d.successful).map((d: { id: number; createdAt: string; paidAmount: number; successful: boolean }) => ({
        ...d, description: ob.description
      }))
    );
    docs = docs.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
    this.lastPayment = docs.length ? docs[0] : null;

    // Sume neplătite per descriere
    const total = this.obligations.reduce((sum, ob) => sum + ob.amount, 0);
    this.unpaidCategories = this.obligations
      .filter(ob => ob.amount - ob.paidAmount > 0)
      .map(ob => ({
        description: ob.description,
        rest: ob.amount - ob.paidAmount,
        percent: total ? Math.round((ob.amount - ob.paidAmount) * 100 / total) : 0
      }));

    // Donut chart
    const paid = this.obligations.reduce((sum, ob) => sum + ob.paidAmount, 0);
    this.donutData = [paid, total - paid > 0 ? total - paid : 0];
  }

  exportExcel() {
    const ws = XLSX.utils.json_to_sheet(this.obligations.map(ob => ({
      Descriere: ob.description,
      Scadenta: ob.dueDate,
      Suma: ob.amount,
      Platit: ob.paidAmount,
      Status: ob.statusLabel
    })));
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Obligatii');
    XLSX.writeFile(wb, 'obligatii.xlsx');
  }

  exportCSV() {
    const rows = [
      ['Descriere', 'Scadenta', 'Suma', 'Platit', 'Status'],
      ...this.obligations.map(ob => [
        ob.description, ob.dueDate, ob.amount, ob.paidAmount, ob.statusLabel
      ])
    ];
    const csv = rows.map(r => r.join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'obligatii.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
