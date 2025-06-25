import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AppointmentComponent } from './appointment/appointment.component';
import { MedicalHistoryComponent } from './medical-history/medical-history.component';
import { ServicesComponent } from './services/services.component';
import { DoctorComponent } from './doctor/doctor.component';
import { DoctorByServiceComponent } from './doctor-by-service/doctor-by-service.component';
import { DoctorProfileComponent } from './doctor-profile/doctor-profile.component';
import { PaymentComponent } from './payment/payment.component';
import { PatientAnalysesComponent } from './patient-analyses/patient-analyses.component';

export const patientRoutes: Routes = [
    {
        path: '',
        component: DashboardComponent
    },
    {
        path: 'appointments',
        component: AppointmentComponent
    },
    {
        path: 'medical-history',
        component: MedicalHistoryComponent
    },
    {
        path: 'services',
        component: ServicesComponent
    },
    {
        path: 'medici',
        component: DoctorComponent
    },
    {
        path: 'services/:serviceId/doctors',
        component: DoctorByServiceComponent
    },
    {
        path: 'doctor/:id',
        component: DoctorProfileComponent
    }, {
        path: 'payments',
        component: PaymentComponent
    },
    {
        path: 'analyses',
        component: PatientAnalysesComponent
    }

];
