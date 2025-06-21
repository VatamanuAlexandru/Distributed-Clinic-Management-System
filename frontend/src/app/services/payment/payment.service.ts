// payment.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PaymentDocumentRecord {
  id: number;
  documentType: string;
  paymentType: string;
  paidAmount: number;
  successful: boolean;
  createdAt: string;
}

export interface PaymentObligationRecord {
  id: number;
  patientId: number;
  amount: number;
  description: string;
  dueDate: string;
  paid: boolean;
  documents: PaymentDocumentRecord[];
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private baseUrl = 'http://localhost:8080/payment/api/payment-obligations';

  constructor(private http: HttpClient) { }

  getPatientObligations(patientId: number): Observable<PaymentObligationRecord[]> {
    return this.http.get<PaymentObligationRecord[]>(`${this.baseUrl}/by-patient/${patientId}`);
  }

  payObligation(document: Partial<PaymentDocumentRecord> & { obligationId: number }): Observable<any> {
    return this.http.post(`${this.baseUrl}/document`, document);
  }
}
