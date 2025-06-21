import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { ThemeService } from '../../../services/theme/theme.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

  pageTitle = '';

  constructor(private router: Router, private themeService: ThemeService) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.setTitle(event.urlAfterRedirects);
      }
    });
  }

  setTitle(url: string) {
    if (url.includes('/dashboard')) this.pageTitle = 'Dashboard';
    else if (url.includes('/pacienti')) this.pageTitle = 'Pacienți';
    else if (url.includes('/medici')) this.pageTitle = 'Medici';
    else if (url.includes('/services')) this.pageTitle = 'Programări';
    else if (url.includes('/medical-history')) this.pageTitle = 'Istoric medical';
    else if (url.includes('/payments')) this.pageTitle = 'Plăți';
    else this.pageTitle = '';
  }

  toggleTheme() {
    this.themeService.toggleTheme();
  }
}
