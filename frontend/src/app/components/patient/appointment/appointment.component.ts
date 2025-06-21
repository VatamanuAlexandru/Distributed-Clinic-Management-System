import { Component } from '@angular/core';
import { AppointmentService } from '../../../services/appointment/appointment.service';
import { UserService } from '../../../services/user-service/user.service';
import { TableComponent } from "../../table/table.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [TableComponent, CommonModule],
  templateUrl: './appointment.component.html',
  styleUrl: './appointment.component.scss'
})
export class AppointmentComponent {
  appointments: any[] = [];
  columns = [
    { key: 'doctorName', label: 'Medic' },
    { key: 'appointmentDate', label: 'Dată' },
    { key: 'status', label: 'Status' },
    {
      key: 'reason', label: 'Motiv'
    }
  ];

  constructor(private appointmentService: AppointmentService, private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getCurrentPatientId().subscribe((patientId: number) => {
      this.appointmentService.getByPatientId(patientId).subscribe(data => {
        this.appointments = data.map(a => ({
          ...a,
          doctorName: a.doctor?.medicalTitle?.name + ' ' + a.doctor?.person?.firstName + ' ' + a.doctor?.person?.lastName,
          appointmentDate: new Date(a.appointmentDate).toLocaleString('ro-RO', {
            weekday: 'long', year: 'numeric', month: 'short', day: 'numeric',
            hour: '2-digit', minute: '2-digit'
          })
        }));
      });
    });
  }


  selectedAppointment: any = null;

  handleAction(event: { type: string; row: any }) {
    if (event.type === 'vezi') {
      this.selectedAppointment = event.row;
      console.log(this.selectedAppointment);
    }
  }

}
