import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';




@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})




export class LoginComponent {
  email = '';
  password = '';
  error = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login() {
    this.authService.login(this.email, this.password).subscribe({
      next: (res) => {
        if (res.role === 'EMPLOYEE') {
          this.router.navigate(['/employee']);
        } else if (res.role === 'MANAGER') {
          this.router.navigate(['/manager']);
        }
      },
      error: () => {
        this.error = 'Email ou mot de passe incorrect';
      }
    });
  }
}
