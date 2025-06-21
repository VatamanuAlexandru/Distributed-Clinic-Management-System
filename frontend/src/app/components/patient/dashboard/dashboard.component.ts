import { AfterViewInit, Component } from '@angular/core';
import { Chart, registerables } from 'chart.js'; // << adăugat aici
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements AfterViewInit {
  ngAfterViewInit(): void {
    Chart.register(...registerables);

    const ctx = document.getElementById('appointmentsChart') as HTMLCanvasElement;
    if (!ctx) {
      console.warn('Canvas-ul nu a fost găsit în DOM');
      return;
    }

    new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['Luni', 'Marți', 'Miercuri', 'Joi', 'Vineri', 'Sâmbătă', 'Duminică'],
        datasets: [{
          label: 'Programări',
          data: [40, 55, 32, 75, 64, 30, 50],
          borderColor: 'rgb(59, 130, 246)',
          backgroundColor: 'rgba(59, 130, 246, 0.2)',
          tension: 0.3,
          fill: true,
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: { color: 'white' }
          },
          x: {
            ticks: { color: 'white' }
          }
        },
        plugins: {
          legend: {
            labels: { color: 'white' }
          }
        }
      }
    });
  }
}
