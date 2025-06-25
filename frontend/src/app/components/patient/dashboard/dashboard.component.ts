import { AfterViewInit, Component } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements AfterViewInit {
  lastUpdated = new Date();

  // Simulare date - aici le poți popula din backend!
  stats = {
    patients: 1245,
    patientsChange: 4.3,
    doctors: 53,
    doctorsChange: 2.1,
    appointmentsToday: 128,
    weekAppointments: 712,
    avgFeedback: 4.7,
    positiveFeedback: 96,
    topDepartments: [
      { name: 'Cardiologie', count: 124 },
      { name: 'Dermatologie', count: 110 },
      { name: 'Pediatrie', count: 95 },
      { name: 'Psihologie', count: 72 },
      { name: 'ORL', count: 54 }
    ]
  };

  recentActivity: string[] = [
    '🟢 Pacientul <strong>Ion Popescu</strong> a fost consultat de <strong>Dr. Lupu</strong> (ORL)',
    '🔵 Programare nouă: <strong>Maria T.</strong> pentru <strong>Psihiatrie</strong>',
    '🟡 Feedback: <strong>Dr. Dumitrescu</strong> – „Foarte profesionist!”',
    '🔴 Anulare: <strong>George I.</strong> (Cardiologie)'
  ];

  ngAfterViewInit(): void {
    Chart.register(...registerables);

    const ctx = document.getElementById('appointmentsChart') as HTMLCanvasElement;
    if (!ctx) return;

    new Chart(ctx, {
      type: 'line',
      data: {
        labels: [
          'Luni', 'Marți', 'Miercuri', 'Joi', 'Vineri', 'Sâmbătă', 'Duminică',
          'Luni', 'Marți', 'Miercuri', 'Joi', 'Vineri', 'Sâmbătă', 'Duminică'
        ],
        datasets: [{
          label: 'Programări',
          data: [40, 55, 32, 75, 64, 30, 50, 42, 60, 38, 80, 77, 35, 52],
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
            ticks: { color: '#64748b' }
          },
          x: {
            ticks: { color: '#64748b' }
          }
        },
        plugins: {
          legend: {
            labels: { color: '#64748b' }
          }
        }
      }
    });
  }
}
