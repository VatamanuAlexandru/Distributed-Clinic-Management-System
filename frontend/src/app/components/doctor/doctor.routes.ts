import { Routes } from '@angular/router';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { AppointmentComponent } from './appointment/appointment.component';
import { DoctorScheduleComponent } from './doctor-schedule/doctor-schedule.component';
import { PatientComponent } from './patient/patient.component';

export const doctorRoutes: Routes = [
  {
    path: '',
    component: DoctorDashboardComponent
  }, {
    path: 'appointments',
    component: AppointmentComponent
  }, {
    path: "schedule",
    component: DoctorScheduleComponent
  }, {
    path: "patients",
    component: PatientComponent
  }

  // {
  //     path: '',
  //     component: DashboardComponent
  // },
  // {
  //     path: 'appointments',
  //     component: AppointmentComponent
  // },
  // {
  //     path: 'medical-history',
  //     component: MedicalHistoryComponent
  // },
  // {
  //     path: 'services',
  //     component: ServicesComponent
  // },
  // {
  //     path: 'medici',
  //     component: DoctorComponent
  // },
  // {
  //     path: 'services/:serviceId/doctors',
  //     component: DoctorByServiceComponent
  // },
  // {
  //     path: 'doctor/:id',
  //     component: DoctorProfileComponent
  // }
];
