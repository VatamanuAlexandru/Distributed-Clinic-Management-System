import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import { DoctorComponent } from './components/doctor/doctor.component';
import { DoctorProfileComponent } from './components/doctor-profile/doctor-profile.component';
import { ServicesComponent } from './components/services/services.component';
import { DoctorByServiceComponent } from './components/doctor-by-service/doctor-by-service.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }, {
    path: 'dashboard',
    component: DashboardComponent
  }, {
    path: 'medici',
    component: DoctorComponent
  }, {
    path: 'doctor/:id',
    component: DoctorProfileComponent
  }, {
    path: 'services',
    component: ServicesComponent
  }, {
    path: 'services/:serviceId/doctors',
    component: DoctorByServiceComponent
  }

];
