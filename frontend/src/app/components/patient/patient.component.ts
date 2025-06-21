import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from "./navbar/navbar.component";

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './patient.component.html',
  styleUrl: './patient.component.scss'
})
export class PatientComponent {

}
