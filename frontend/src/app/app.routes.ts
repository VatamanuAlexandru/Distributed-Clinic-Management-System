import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { PatientComponent } from './components/patient/patient.component';
import { patientRoutes } from './components/patient/patient.routes';
import { DoctorComponent } from './components/doctor/doctor.component';
import { doctorRoutes } from './components/doctor/doctor.routes';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'patient',
    component: PatientComponent,
    children: patientRoutes
  },
  {
    path: 'doctor',
    component: DoctorComponent,
    children: doctorRoutes
  }

];
