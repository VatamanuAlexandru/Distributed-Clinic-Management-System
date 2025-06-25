import { Component } from '@angular/core';
import { AppointmentService } from '../../../services/appointment/appointment.service';
import { UserService } from '../../../services/user-service/user.service';
import { TableComponent } from "../../table/table.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [TableComponent, CommonModule, FormsModule],
  templateUrl: './appointment.component.html',
  styleUrl: './appointment.component.scss'
})
export class AppointmentComponent {
  appointments: any[] = [];
  filteredAppointments: any[] = [];
  columns = [
    { key: 'doctorName', label: 'Medic' },
    { key: 'appointmentDate', label: 'Dată' },
    { key: 'status', label: 'Status' },
    { key: 'reason', label: 'Motiv' }
  ];
  statusFilter: '' | 'CONFIRMED' | 'PENDING' | 'CANCELLED' = '';
  searchText: string = '';
  confirmedCount = 0;
  pendingCount = 0;
  cancelledCount = 0;
  selectedAppointment: any = null;
  nextAppointment: any = null;
  appointmentStats = [
    { label: 'Total', value: this.appointments.length, icon: 'event_note', color: 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-200', iconColor: 'text-blue-600' },
    { label: 'Confirmate', value: this.confirmedCount, icon: 'check_circle', color: 'bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-200', iconColor: 'text-green-600' },
    { label: 'În așteptare', value: this.pendingCount, icon: 'hourglass_empty', color: 'bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-200', iconColor: 'text-yellow-600' },
    { label: 'Anulate', value: this.cancelledCount, icon: 'cancel', color: 'bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-200', iconColor: 'text-red-600' },
  ];
  

  constructor(private appointmentService: AppointmentService, private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getCurrentPatientId().subscribe((patientId: number) => {
      this.appointmentService.getByPatientId(patientId).subscribe(data => {
        this.appointments = (data || []).map(a => ({
          ...a,
          doctorName: (a.doctor?.medicalTitle?.name ? a.doctor.medicalTitle.name + ' ' : '') +
            (a.doctor?.person?.firstName || '') + ' ' +
            (a.doctor?.person?.lastName || ''),
          appointmentDate: new Date(a.appointmentDate).toLocaleString('ro-RO', {
            weekday: 'long', year: 'numeric', month: 'short', day: 'numeric',
            hour: '2-digit', minute: '2-digit'
          }),
          rawDate: a.appointmentDate 
        }));
        this.calcStats();
        this.filterAppointments();
        this.setNextAppointment();
      });
    });
  }

  handleAction(event: { type: string; row: any }) {
    if (event.type === 'vezi') {
      this.selectedAppointment = event.row;
    }
  }

  filterAppointments() {
    let result = this.appointments;

    if (this.statusFilter) {
      result = result.filter(a => a.status === this.statusFilter);
    }
    if (this.searchText) {
      const txt = this.searchText.toLowerCase();
      result = result.filter(a =>
        (a.doctorName && a.doctorName.toLowerCase().includes(txt)) ||
        (a.reason && a.reason.toLowerCase().includes(txt)) ||
        (a.status && a.status.toLowerCase().includes(txt)) ||
        (a.appointmentDate && a.appointmentDate.toLowerCase().includes(txt))
      );
    }
    this.filteredAppointments = result;
  }

  calcStats() {
    this.confirmedCount = this.appointments.filter(a => a.status === 'CONFIRMED').length;
    this.pendingCount = this.appointments.filter(a => a.status === 'PENDING').length;
    this.cancelledCount = this.appointments.filter(a => a.status === 'CANCELLED').length;
  }

  setNextAppointment() {
    const now = new Date();
    this.nextAppointment = this.appointments
      .filter(a => a.status !== 'CANCELLED' && new Date(a.rawDate) > now)
      .sort((a, b) => +new Date(a.rawDate) - +new Date(b.rawDate))[0] || null;
  }
}
