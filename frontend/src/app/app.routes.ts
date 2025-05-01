import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import { DoctorComponent } from './components/doctor/doctor.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },{
    path: 'dashboard',
    component: DashboardComponent
  },   { path: 'medici', component: DoctorComponent }

];
