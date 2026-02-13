import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-stats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-stats.html',
  styleUrls: ['./admin-stats.css']
})
export class AdminStats implements OnInit {

  monthly: any[] = [];
  employees: any[] = [];
  loading = false;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats() {
    this.loading = true;

    forkJoin({
      monthly: this.http.get<any[]>('http://localhost:8081/api/admin/stats/monthly'),
      employees: this.http.get<any[]>('http://localhost:8081/api/admin/stats/employees')
    }).subscribe({
      next: ({ monthly, employees }) => {
        this.monthly = monthly ?? [];
        this.employees = employees ?? [];
        this.loading = false;

        // 🔥 Force Angular refresh
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('Stats error', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
  getTotalPaid(): number {
    return this.monthly.reduce((sum, m) => sum + (m.total || 0), 0);
  }
  
  getBestMonth() {
    if (!this.monthly.length) return null;
    return this.monthly.reduce((prev, current) =>
      (prev.total > current.total) ? prev : current
    );
  }
  
  getTopEmployee() {
    if (!this.employees.length) return null;
    return this.employees.reduce((prev, current) =>
      (prev.total > current.total) ? prev : current
    );
  }
}
