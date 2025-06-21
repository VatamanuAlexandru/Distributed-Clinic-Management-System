import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { ThemeService } from '../../../services/theme/theme.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  pageTitle = '';

  constructor(private router: Router, private themeService: ThemeService) {
    // Subscribe to router events to update the page title dynamically
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.setTitle(event.urlAfterRedirects);
      }
    });
  }

  // Set the page title based on the current route
  setTitle(url: string) {
    if (url.includes('/doctor/dashboard')) this.pageTitle = 'Dashboard Medic';
    else if (url.includes('/doctor/appointments')) this.pageTitle = 'Programări';
    else if (url.includes('/doctor/medical-history')) this.pageTitle = 'Istoric Medical';
    else if (url.includes('/doctor/services')) this.pageTitle = 'Servicii';
    else if (url.includes('/doctor/patients')) this.pageTitle = 'Pacienți';
    else this.pageTitle = '';
  }

  // Toggle the theme between light and dark modes
  toggleTheme() {
    this.themeService.toggleTheme();
  }
}