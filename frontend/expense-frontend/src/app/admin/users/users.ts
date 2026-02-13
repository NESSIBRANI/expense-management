import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { User } from '../models/user.model';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.html',
  styleUrls: ['./users.css']
})
export class Users implements OnInit {

  users: User[] = [];
  loading = true;
  search = '';

  constructor(
    private userService: UserService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;

    this.userService.getAll().subscribe({
      next: data => {
        this.users = data ?? [];
        this.loading = false;

        // 🔥 FORCE Angular to refresh view
        this.cdr.detectChanges();
      },
      error: err => {
        console.error(err);
        this.users = [];
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  filteredUsers(): User[] {
    return this.users.filter(u =>
      `${u.name} ${u.email} ${u.role}`
        .toLowerCase()
        .includes(this.search.toLowerCase())
    );
  }

  toggle(user: User) {
    const updatedUser = {
      ...user,
      enabled: !user.enabled
    };

    this.userService.update(updatedUser).subscribe({
      next: () => {
        user.enabled = !user.enabled;
        this.cdr.detectChanges();
      }
    });
  }

  exportUsersToCSV() {
    const headers = ['Name', 'Email', 'Role', 'Status'];

    const rows = this.users.map(u => [
      u.name,
      u.email,
      u.role,
      u.enabled ? 'Active' : 'Disabled'
    ]);

    const csvContent =
      [headers, ...rows]
        .map(row => row.join(','))
        .join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = 'users.csv';
    link.click();
  }
}
