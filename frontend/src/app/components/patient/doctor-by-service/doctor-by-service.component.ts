import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import { DoctorService } from '../../../services/doctor/doctor.service';
import { DoctorScheduleService } from '../../../services/doctor-schedule/doctor-schedule.service';
import { FullCalendarComponent, FullCalendarModule } from '@fullcalendar/angular';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { CalendarOptions } from '@fullcalendar/core';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../../services/appointment/appointment.service';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'app-doctor-by-service',
  imports: [CommonModule, FullCalendarModule, FormsModule],
  templateUrl: './doctor-by-service.component.html',
  styleUrl: './doctor-by-service.component.scss',
})
export class DoctorByServiceComponent implements OnInit {
  serviceId!: number;
  doctors: any[] = [];

  selectedDoctorId: number | null = null;
  selectedDate: Date | null = null;
  selectedSlot: string | null = null;
  appointmentReason: string = '';
  appointmentNotes: string = '';

  successMessage: string = '';
  errorMessage: string = '';
  feedbackTimeout: any;
  patientId!: number;

  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;

  calendarOptions: CalendarOptions = {
    plugins: [timeGridPlugin, interactionPlugin],
    initialView: 'timeGridDay',
    slotMinTime: '08:00:00',
    slotMaxTime: '18:00:00',
    slotDuration: '00:30:00',
    nowIndicator: true,
    allDaySlot: false,
    contentHeight: 'auto',
    expandRows: true,
    headerToolbar: { left: '', center: 'title', right: '' },
    events: [],
    eventClick: this.handleSlotClick.bind(this)
  };

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private doctorService: DoctorService,
    private doctorScheduleService: DoctorScheduleService,
    private appointmentService: AppointmentService,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.serviceId = Number(params.get('serviceId'));
      this.doctorService.getDoctorsByService(this.serviceId).subscribe(data => {
        this.doctors = data;
      });
    });
  }

  back(): void {
    this.location.back();
  }

  openScheduleModal(doctorId: number) {
    this.selectedDoctorId = doctorId;
    this.selectedDate = null;
    this.selectedSlot = null;
    this.appointmentReason = '';
    this.clearFeedback();
    const calendarApi = this.calendarComponent?.getApi();
    calendarApi?.removeAllEvents();
  }

  onDateSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selected = input.valueAsDate;
    if (!selected || !this.selectedDoctorId) return;

    this.selectedDate = selected;
    const iso = selected.toISOString().split('T')[0] + 'T00:00:00';
    const calendarApi = this.calendarComponent.getApi();
    calendarApi.removeAllEvents();

    this.doctorScheduleService.getAvailableSlots(this.selectedDoctorId, this.serviceId, iso).subscribe(available => {
      this.doctorScheduleService.getOcupiedSlots(this.selectedDoctorId!, this.serviceId, iso).subscribe(occupied => {
        const events = [
          ...available.map(slot => ({
            title: 'Liber',
            start: slot,
            end: new Date(new Date(slot).getTime() + 30 * 60000),
            backgroundColor: '#38b2ac',
            borderColor: '#319795'
          })),
          ...occupied.map(slot => ({
            title: 'Ocupat',
            start: slot,
            end: new Date(new Date(slot).getTime() + 30 * 60000),
            backgroundColor: '#f56565',
            borderColor: '#c53030'
          }))
        ];
        events.forEach(e => calendarApi.addEvent(e));
        calendarApi.gotoDate(selected);
      });
    });
  }

  handleSlotClick(arg: any): void {
    if (arg.event.title === 'Ocupat') return;
    const calendarApi = this.calendarComponent.getApi();
    calendarApi.getEvents().forEach(event => {
      if (event.title === 'Liber') {
        event.setProp('backgroundColor', '#38b2ac');
        event.setProp('borderColor', '#319795');
      }
    });
    arg.event.setProp('backgroundColor', '#2563eb');
    arg.event.setProp('borderColor', '#1e40af');
    this.selectedSlot = arg.event.startStr;
  }

  confirmAppointment(): void {
    if (!this.selectedDoctorId || !this.selectedSlot || !this.appointmentReason.trim()) return;

    this.http.get<number>('http://localhost:8080/user-service/api/auth/currentPatientId')
      .subscribe(patientId => {
        this.patientId = patientId;
      });

    const localDate = new Date(this.selectedSlot!);
    const localISO = `${localDate.getFullYear()}-${(localDate.getMonth() + 1).toString().padStart(2, '0')}-${localDate.getDate().toString().padStart(2, '0')}T${localDate.getHours().toString().padStart(2, '0')}:${localDate.getMinutes().toString().padStart(2, '0')}:00`; const payload = {
      doctorId: this.selectedDoctorId,
      patientId: this.patientId,
      serviceId: this.serviceId,
      reason: this.appointmentReason,
      appointmentDate: localISO
    };

    this.appointmentService.createAppointment(payload).subscribe({
      next: () => {
        const formatted = new Date(this.selectedSlot!).toLocaleString('ro-RO', {
          weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
          hour: '2-digit', minute: '2-digit'
        });
        this.successMessage = `✅ Programare confirmată pentru ${formatted}.`;
        this.errorMessage = '';
        this.autoClearFeedback();

        setTimeout(() => {
          this.selectedDoctorId = null;
        }, 300);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = '❌ Eroare la confirmarea programării. Încearcă din nou.';
        this.successMessage = '';
        this.autoClearFeedback();
      }
    });

  }

  get selectedDoctor() {
    return this.doctors.find(d => d.id === this.selectedDoctorId);
  }

  hasFeedback(): boolean {
    return !!this.successMessage || !!this.errorMessage;
  }

  autoClearFeedback(): void {
    if (this.feedbackTimeout) clearTimeout(this.feedbackTimeout);
    this.feedbackTimeout = setTimeout(() => {
      this.clearFeedback();
    }, 50000);
  }

  clearFeedback(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }
}

