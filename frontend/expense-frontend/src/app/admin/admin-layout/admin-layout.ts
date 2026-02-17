import { Component, ViewEncapsulation, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, MatButtonModule],
  templateUrl: './admin-layout.html',
  styleUrls: ['./admin-layout.css'],
  encapsulation: ViewEncapsulation.None
})
export class AdminLayout {

  private authService = inject(AuthService);
  private router = inject(Router);

  logout() {
    console.log("LOGOUT CLICKED"); // pour vérifier

    this.authService.logout();

    // navigation FORCÉE
    this.router.navigateByUrl('/login');
  }
}
