import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableComponent } from '../../table/table.component';
import { AppointmentService } from '../../../services/appointment/appointment.service';
import { UserService } from '../../../services/user-service/user.service';

@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [CommonModule, FormsModule, TableComponent],
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent {
  appointments: any[] = [];
  filteredAppointments: any[] = [];

  columns = [
    { key: 'patientName', label: 'Pacient' },
    { key: 'appointmentDate', label: 'Dată' },
    { key: 'statusTranslated', label: 'Status' },
    { key: 'reason', label: 'Motiv' }
  ];

  actions = ['vezi', 'acceptă', 'respinge', 'finalizează'];

  selectedAppointment: any = null;
  searchQuery: string = '';
  selectedStatus: string = '';

  toastMessage: string | null = null;
  toastSuccess: boolean = true;

  statusTabs = [
    { value: '', label: 'Toate' },
    { value: 'PENDING', label: 'În așteptare' },
    { value: 'CONFIRMED', label: 'Confirmate' },
    { value: 'CANCELED', label: 'Anulate' },
    { value: 'COMPLETED', label: 'Finalizate' }
  ];

  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private appointmentService: AppointmentService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.userService.getCurrentDoctorId().subscribe((doctorId: number) => {
      this.appointmentService.getByDoctorId(doctorId).subscribe(data => {
        this.appointments = data.map(a => ({
          ...a,
          patientName: `${a.patient?.person?.firstName} ${a.patient?.person?.lastName}`,
          appointmentDate: new Date(a.appointmentDate).toLocaleString('ro-RO', {
            weekday: 'long',
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
          }),
          statusTranslated: this.translateStatus(a.status),
          rowClass: this.getRowClass(a.status)
        }));
        this.filteredAppointments = [...this.appointments];
        this.filterAppointments();
      });
    });
  }

  // ===== ACȚIUNI =====
  handleAction(event: { type: string; row: any }) {
    const appointment = event.row;
    switch (event.type) {
      case 'vezi':
        this.selectedAppointment = appointment;
        break;
      case 'acceptă':
        this.updateStatus(appointment.id, 'CONFIRMED');
        break;
      case 'respinge':
        this.updateStatus(appointment.id, 'CANCELED');
        break;
      case 'finalizează':
        this.updateStatus(appointment.id, 'COMPLETED');
        break;
    }
  }

  updateStatus(appointmentId: number, status: string): void {
    this.appointmentService.updateStatus(appointmentId, status).subscribe({
      next: () => {
        const appt = this.appointments.find(a => a.id === appointmentId);
        if (appt) {
          appt.status = status;
          appt.statusTranslated = this.translateStatus(status);
          appt.rowClass = this.getRowClass(status);
          this.filterAppointments();
        }
        this.showToast(`Status actualizat la „${this.translateStatus(status)}”`, true);
      },
      error: err => {
        console.error('Eroare la actualizarea statusului:', err);
        this.showToast('A apărut o eroare la actualizarea statusului.', false);
      }
    });
  }

  // ===== FILTRARE =====
  onSearchChange(query: string): void {
    this.searchQuery = query;
    this.filterAppointments();
  }

  onStatusChange(status: string): void {
    this.selectedStatus = status;
    this.filterAppointments();
  }

  onStatusTabChange(status: string): void {
    this.selectedStatus = status;
    this.filterAppointments();
  }

  resetSearch(): void {
    this.searchQuery = '';
    this.filterAppointments();
  }

  filterAppointments(): void {
    this.filteredAppointments = this.appointments.filter(appt => {
      const matchesSearch = appt.patientName.toLowerCase().includes(this.searchQuery.toLowerCase());
      const matchesStatus = this.selectedStatus
        ? appt.status.toLowerCase() === this.selectedStatus.toLowerCase()
        : true;
      return matchesSearch && matchesStatus;
    });
    this.applySorting();
  }

  getCountByStatus(status: string): number {
    if (!status) return this.appointments.length;
    return this.appointments.filter(a => a.status === status).length;
  }

  // ===== SORTARE =====
  onSort(event: { key: string; direction: 'asc' | 'desc' }): void {
    this.sortColumn = event.key;
    this.sortDirection = event.direction;
    this.applySorting();
  }

  applySorting(): void {
    if (!this.sortColumn) return;
    this.filteredAppointments.sort((a, b) => {
      const valueA = a[this.sortColumn];
      const valueB = b[this.sortColumn];

      if (valueA == null || valueB == null) return 0;

      const compare = typeof valueA === 'string'
        ? valueA.localeCompare(valueB)
        : valueA > valueB ? 1 : valueA < valueB ? -1 : 0;

      return this.sortDirection === 'asc' ? compare : -compare;
    });
  }

  // ===== UTILE =====
  translateStatus(status: string): string {
    switch (status) {
      case 'PENDING': return 'În așteptare';
      case 'CONFIRMED': return 'Confirmată';
      case 'CANCELED': return 'Anulată';
      case 'COMPLETED': return 'Finalizată';
      default: return status;
    }
  }

  getRowClass(status: string): string {
    switch (status) {
      case 'CONFIRMED': return 'bg-green-50 dark:bg-green-900';
      case 'CANCELED': return 'bg-red-50 dark:bg-red-900';
      case 'PENDING': return 'bg-gray-50 dark:bg-gray-800';
      case 'COMPLETED': return 'bg-indigo-50 dark:bg-indigo-900';
      default: return '';
    }
  }

  // ===== TOAST =====
  showToast(message: string, success: boolean): void {
    this.toastMessage = message;
    this.toastSuccess = success;
    setTimeout(() => this.toastMessage = null, 3000);
  }

  exportToCSV(): void {
    if (!this.filteredAppointments.length) return;
  
    const header = this.columns.map(c => c.label);
    const rows = this.filteredAppointments.map(appt =>
      this.columns.map(c => `"${(appt[c.key] ?? '').toString().replace(/"/g, '""')}"`)
    );
  
    const csvContent = [
      header.join(','),
      ...rows.map(r => r.join(','))
    ].join('\n');
  
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `programari_${new Date().toISOString().slice(0,10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
  
}
