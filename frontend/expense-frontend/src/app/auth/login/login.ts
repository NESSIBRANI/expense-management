import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    RouterModule, 
    MatProgressSpinnerModule
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  email = '';
  password = '';
  message = '';
  loading = false;

  private authService = inject(AuthService);
  private router = inject(Router);

  onLogin(): void {

    if (!this.email || !this.password) {
      this.message = 'Veuillez remplir tous les champs';
      return;
    }

    this.loading = true;
    this.message = '';

    // ⭐ IMPORTANT : login inclut déjà loadUser
    this.authService.login(this.email, this.password).subscribe({
      next: () => {

        const role = this.authService.getRole();
        if (!role) {
          this.router.navigate(['/login']);
         return;
}

this.redirectByRole(role);

      },
      error: () => {
        this.message = 'Email ou mot de passe incorrect';
        this.loading = false;
      }
    });
  }

 redirectByRole(role: string) {

  if (role === 'ADMIN') {
    this.router.navigate(['/admin']);
  }

  else if (role === 'MANAGER') {
    this.router.navigate(['/manager']);
  }

  else {
    this.router.navigate(['/employee']);
  }
}

}
