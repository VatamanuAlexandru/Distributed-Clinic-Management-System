import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-no-content',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './no-content.component.html',
  styleUrl: './no-content.component.scss'
})
export class NoContentComponent {
  @Input() message: string = 'Nu am găsit nicio înregistrare.';

}
