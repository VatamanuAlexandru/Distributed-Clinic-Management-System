import { Component, OnInit } from '@angular/core';
import { PaymentObligationRecord, PaymentService } from '../../../services/payment/payment.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule]
})
export class PaymentComponent implements OnInit {
  obligations: PaymentObligationRecord[] = [];
  filteredObligations: PaymentObligationRecord[] = [];
  loading = true;
  patientId: number | null = null;
  selectedObligation: PaymentObligationRecord | null = null;
  toast: { message: string, type: 'success' | 'error' } | null = null;
  errorMessage: string | null = null;
  filterStatus: 'all' | 'paid' | 'partial' | 'unpaid' = 'all';
  page: number = 1;
  pageSize: number = 6;

  constructor(
    private paymentService: PaymentService,
    private authService: AuthService
  ) { }
  ngOnInit() {
    console.log("IN ON INIT"); // <-- vezi dacă intri aici!
    this.authService.getCurrentPatientId().subscribe({
      next: id => {
        console.log('GOT PATIENT ID:', id); // <--- vezi dacă primești ID
        this.patientId = id;
        this.fetchObligations();
      },
      error: (err) => {
        console.log('ERROR GETTING PATIENT ID', err); // <---
        this.showToast('Eroare la identificarea pacientului!', 'error');
        this.errorMessage = 'Nu am putut identifica utilizatorul. Te rugăm să încerci din nou.';
        this.loading = false;
      }
    });
  }

  fetchObligations() {
    if (this.patientId === null) return;
    this.loading = true;
    this.errorMessage = null;
    this.paymentService.getPatientObligations(this.patientId).subscribe({
      next: data => {
        console.log('OBLIGATIONS:', data); // <---- AICI
        this.obligations = data;
        this.applyFilter();
        this.loading = false;
      },
      error: (err) => {
        console.log('ERROR:', err); // <---- AICI
        this.showToast('Eroare la încărcarea plăților!', 'error');
        this.errorMessage = 'Am întâmpinat o problemă la încărcarea plăților. Te rugăm să încerci din nou.';
        this.loading = false;
      }
    });
  }


  payNow(obligation: PaymentObligationRecord) {
    const payload = {
      obligationId: obligation.id,
      documentType: 'RECEIPT',
      paymentType: 'CARD',
      paidAmount: obligation.amount - this.getPaidAmount(obligation),
      successful: true
    };
    this.paymentService.payObligation(payload).subscribe({
      next: () => {
        this.showToast('Plata efectuată cu succes!', 'success');
        this.fetchObligations();
      },
      error: () => {
        this.showToast('Eroare la procesarea plății!', 'error');
        this.errorMessage = 'Nu am putut procesa plata. Te rugăm să verifici conexiunea și să încerci din nou.';
      }
    });
  }

  openDetails(obligation: PaymentObligationRecord) {
    this.selectedObligation = obligation;
  }

  closeDetails() {
    this.selectedObligation = null;
  }

  getPaidAmount(obligation: PaymentObligationRecord): number {
    return obligation.documents?.filter(d => d.successful).reduce((s, d) => s + (d.paidAmount || 0), 0) || 0;
  }

  getStatus(obligation: PaymentObligationRecord): 'paid' | 'partial' | 'unpaid' {
    if (obligation.paid) return 'paid';
    const paid = this.getPaidAmount(obligation);
    if (paid > 0) return 'partial';
    return 'unpaid';
  }

  showToast(message: string, type: 'success' | 'error') {
    this.toast = { message, type };
    setTimeout(() => this.toast = null, 2500);
  }

  getPaidPercentage(): number {
    if (this.obligations.length === 0) return 0;
    const totalPaid = this.obligations.reduce((sum, ob) => sum + this.getPaidAmount(ob), 0);
    const totalAmount = this.obligations.reduce((sum, ob) => sum + ob.amount, 0);
    return Math.round((totalPaid / totalAmount) * 100) || 0;
  }

  getTotalPaid(): number {
    return this.obligations.reduce((sum, ob) => sum + this.getPaidAmount(ob), 0) || 0;
  }

  getTotalDue(): number {
    return this.obligations.reduce((sum, ob) => sum + (ob.amount - this.getPaidAmount(ob)), 0) || 0;
  }

  getRecentPayments(): any[] {
    const payments = [];
    for (const ob of this.obligations) {
      if (ob.documents && ob.documents.length > 0) {
        for (const doc of ob.documents) {
          if (doc.successful) {
            payments.push({
              description: ob.description,
              documentType: doc.documentType,
              paymentType: doc.paymentType,
              paidAmount: doc.paidAmount,
              createdAt: doc.createdAt
            });
          }
        }
      }
    }
    return payments.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()).slice(0, 3);
  }

  setFilter(status: 'all' | 'paid' | 'partial' | 'unpaid') {
    this.filterStatus = status;
    this.page = 1;
    this.applyFilter();
  }

  applyFilter() {
    this.filteredObligations = this.obligations.filter(ob => {
      if (this.filterStatus === 'all') return true;
      return this.getStatus(ob) === this.filterStatus;
    });
  }

  changePage(delta: number) {
    this.page += delta;
    if (this.page < 1) this.page = 1;
    const maxPage = Math.ceil(this.filteredObligations.length / this.pageSize);
    if (this.page > maxPage) this.page = maxPage;
  }

  calculateTotalPages(totalItems: number, pageSize: number): number {

    return Math.ceil(totalItems / pageSize);

  }
}