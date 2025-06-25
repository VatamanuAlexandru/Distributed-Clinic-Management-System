import { Component, OnInit, OnDestroy } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { ThemeService } from '../../../services/theme/theme.service';
import { AuthService } from '../../../services/auth/auth.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [RouterModule, CommonModule]
})
export class NavbarComponent implements OnInit, OnDestroy {
  pageTitle = '';
  notifications: any[] = [];
  unreadCount = 0;
  showDropdown = false;
  notifSubscription?: Subscription;
  userId?: number;

  constructor(
    public router: Router,  // public dacă îl folosești în template
    public themeService: ThemeService,
    public authService: AuthService,
    public notificationService: NotificationService
  ) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.setTitle(event.urlAfterRedirects);
      }
    });
  }

  ngOnInit() {
    this.authService.getCurrentUserId().subscribe(userId => {
      this.userId = userId;
      this.notificationService.connect(userId);
      this.notifSubscription = this.notificationService.notifications$.subscribe(notifs => {
        this.notifications = notifs;
        this.unreadCount = notifs.filter(n => !n.read).length;
      });
    });
  }

  ngOnDestroy() {
    this.notifSubscription?.unsubscribe();
    this.notificationService.disconnect();
  }

  setTitle(url: string) {
    if (url.includes('/patient') && !url.includes('/medici') && !url.includes('/services') && !url.includes('/appointments') && !url.includes('/medical-history') && !url.includes('/payments')) {
      this.pageTitle = 'Dashboard';
    } else if (url.includes('/medici')) {
      this.pageTitle = 'Medici';
    } else if (url.includes('/services')) {
      this.pageTitle = 'Servicii';
    } else if (url.includes('/appointments')) {
      this.pageTitle = 'Programări';
    } else if (url.includes('/medical-history')) {
      this.pageTitle = 'Istoric medical';
    } else if (url.includes('/payments')) {
      this.pageTitle = 'Plăți';
    } else {
      this.pageTitle = '';
    }
  }

  toggleTheme() {
    this.themeService.toggleTheme();
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  markAllAsRead() {
    this.notifications.forEach(n => n.read = true);
    this.unreadCount = 0;
  }
}
