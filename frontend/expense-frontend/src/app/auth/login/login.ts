import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MaterialModule } from '../../material.module';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MaterialModule   // ✅ BIEN ICI
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  email = '';
  password = '';
  message = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

onLogin() {
  this.loading = true;
  this.message = '';

  this.authService.login({
    email: this.email,
    password: this.password
  }).subscribe({
    next: (res) => {

      // 🔐 Stocker le token
      localStorage.setItem('token', res.token);

      // 🔎 Décoder le JWT
      const payload = JSON.parse(atob(res.token.split('.')[1]));

      // ✅ STOCKER LES INFOS IMPORTANTES
      localStorage.setItem('role', payload.role);
      localStorage.setItem('userId', payload.userId); // ✅ IMPORTANT

      // 🔀 Redirection
      this.redirectByRole(payload.role);
    },
    error: () => {
      this.message = '❌ Login échoué';
      this.loading = false;
    }
  });
}


  private redirectByRole(role: string) {
    this.loading = false;

    if (role === 'EMPLOYEE') {
      this.router.navigate(['/employee']);
    } else if (role === 'MANAGER') {
      this.router.navigate(['/manager']);
    } else {
      this.router.navigate(['/unauthorized']);
    }
  }
}
