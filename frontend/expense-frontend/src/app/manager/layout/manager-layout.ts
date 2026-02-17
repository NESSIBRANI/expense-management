import { Component, inject } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-manager-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    RouterOutlet,   // ⭐⭐⭐⭐⭐ TRÈS IMPORTANT
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatButtonModule
  ],
  templateUrl: './manager-layout.html',
  styleUrls: ['./manager-layout.css']
})
export class ManagerLayoutComponent {

  private auth = inject(AuthService);
  private router = inject(Router);

  opened = true;

  get name(): string {
    return localStorage.getItem('name') || 'Manager';
  }

  toggleSidebar(){
    this.opened = !this.opened;
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}