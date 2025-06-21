import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { HttpClientModule } from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: 'login.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  providers: [HttpClientModule]
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.login(email, password).subscribe({
        next: (res) => {
          this.authService.saveToken(res.token);
          this.authService.saveRoles(res.roles);
          console.log(res.roles);

          const roles = res.roles;

          if (roles.includes('PACIENT')) {
            this.router.navigate(['/patient']);
          } else if (roles.includes('ADMIN')) {
            this.router.navigate(['/admin']);
          } else if (roles.includes('DOCTOR')) {
            this.router.navigate(['/doctor']);
          } else {
            this.router.navigate(['/login']);
          }          
        },
        error: (err) => {
          this.errorMessage = 'Invalid credentials';
          console.error(err);
        }
      });
      // this.loginForm.markAllAsTouched();
    }
  }
}
