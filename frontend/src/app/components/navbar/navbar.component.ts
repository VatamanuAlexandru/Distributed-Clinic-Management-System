import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule], // 🔥 importă RouterModule aici!
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

  pageTitle = '';

  constructor(private router: Router) {
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
    else if (url.includes('/programari')) this.pageTitle = 'Programări';
    else this.pageTitle = '';
  }
}
