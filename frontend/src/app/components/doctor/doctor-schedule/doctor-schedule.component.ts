import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DoctorScheduleService } from '../../../services/doctor-schedule/doctor-schedule.service';
import { addDays, startOfWeek, format } from 'date-fns';
import { AuthService } from '../../../services/auth/auth.service';

interface WeekDay { day: string; label: string; date: Date; }
interface Schedule { id?: number; dayOfWeek: string; startTime: string; endTime: string; }
interface DayOption { value: string; label: string; }

@Component({
  selector: 'app-doctor-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctor-schedule.component.html',
  styleUrls: ['./doctor-schedule.component.scss']
})
export class DoctorScheduleComponent implements OnInit {
  hours: number[] = [];
  hourHeightPx = 40;
  weekStart: Date = startOfWeek(new Date(), { weekStartsOn: 1 });
  weekDays: WeekDay[] = [];
  schedules: Schedule[] = [];
  doctorId?: number;

  // select options for modal form
  dayOptions: DayOption[] = [
    { value: 'MONDAY', label: 'Luni' },
    { value: 'TUESDAY', label: 'Marți' },
    { value: 'WEDNESDAY', label: 'Miercuri' },
    { value: 'THURSDAY', label: 'Joi' },
    { value: 'FRIDAY', label: 'Vineri' },
    { value: 'SATURDAY', label: 'Sâmbătă' },
    { value: 'SUNDAY', label: 'Duminică' }
  ];

  // Modal & Form state
  modalOpen = false;
  editingSchedule: Schedule | null = null;
  form: Schedule = { dayOfWeek: '', startTime: '', endTime: '' };

  constructor(
    private doctorScheduleService: DoctorScheduleService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    // build hours array 6-22
    for (let h = 6; h <= 22; h++) {
      this.hours.push(h);
    }
    this.generateWeekDays();
    // get doctor id from auth
    this.authService.getCurrentDoctorId().subscribe(id => {
      this.doctorId = id;
      this.loadSchedules();
    });
  }

  generateWeekDays(): void {
    this.weekDays = [];
    for (let i = 0; i < 7; i++) {
      const date = addDays(this.weekStart, i);
      this.weekDays.push({
        day: date.toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
        label: format(date, 'EEE'),
        date
      });
    }
  }

  get weekLabel(): string {
    const start = format(this.weekStart, 'dd MMM');
    const end = format(addDays(this.weekStart, 6), 'dd MMM');
    return `${start} - ${end}`;
  }

  prevWeek(): void {
    this.weekStart = addDays(this.weekStart, -7);
    this.generateWeekDays();
  }
  nextWeek(): void {
    this.weekStart = addDays(this.weekStart, 7);
    this.generateWeekDays();
  }

  isToday(date: Date): boolean {
    return date.toDateString() === new Date().toDateString();
  }

  loadSchedules(): void {
    if (!this.doctorId) return;
    this.doctorScheduleService.getDoctorSchedule(this.doctorId)
      .subscribe(data => this.schedules = data);
  }

  schedulesByDay(dayValue: string): Schedule[] {
    return this.schedules.filter(s => s.dayOfWeek === dayValue);
  }

  timeToPixels(time: string): number {
    const [h, m] = time.split(':').map(Number);
    return ((h - 6) + m / 60) * this.hourHeightPx + this.hourHeightPx;
  }

  durationToPixels(start: string, end: string): number {
    const [sh, sm] = start.split(':').map(Number);
    const [eh, em] = end.split(':').map(Number);
    return (eh + em / 60 - (sh + sm / 60)) * this.hourHeightPx;
  }

  openAddScheduleModal(): void {
    this.editingSchedule = null;
    this.form = { dayOfWeek: 'MONDAY', startTime: '09:00', endTime: '17:00' };
    this.modalOpen = true;
  }

  openEditScheduleModal(s: Schedule): void {
    this.editingSchedule = s;
    this.form = { ...s };
    this.modalOpen = true;
  }

  closeModal(): void {
    this.modalOpen = false;
  }

  saveSchedule(): void {
    if (!this.doctorId) return;
    const payload = { doctor: { id: this.doctorId }, ...this.form };
    if (this.editingSchedule) {
      // delete then add to update
      this.doctorScheduleService.deleteSchedule(this.editingSchedule.id!)
        .subscribe(() => this.doctorScheduleService.addSchedule(payload)
          .subscribe(() => {
            this.loadSchedules();
            this.closeModal();
          })
        );
    } else {
      this.doctorScheduleService.addSchedule(payload)
        .subscribe(() => {
          this.loadSchedules();
          this.closeModal();
        });
    }
  }
}
