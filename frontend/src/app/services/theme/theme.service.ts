import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private darkModeKey = 'dark-mode';

  isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }

  toggleTheme() {
    if (!this.isBrowser()) return;
    const isDark = document.documentElement.classList.toggle('dark');
    localStorage.setItem(this.darkModeKey, isDark ? 'true' : 'false');
  }

  initializeTheme() {
    if (!this.isBrowser()) return;
    const isDark = localStorage.getItem(this.darkModeKey) === 'true';
    document.documentElement.classList.toggle('dark', isDark);
  }
}
