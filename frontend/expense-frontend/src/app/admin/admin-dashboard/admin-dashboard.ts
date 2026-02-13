import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AdminService } from '../services/admin.service';
import { UserService } from '../services/user.service';


Chart.register(...registerables);

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css'],
})
export class AdminDashboard implements OnInit {

  reports: any[] = [];
  recentUsers: any[] = [];

  stats = {
    total: 0,
    paid: 0,
    approved: 0,
    rejected: 0
  };

  private expensesChart?: Chart;
  private usersChart?: Chart;

  constructor(
    private adminService: AdminService,
    private userService: UserService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
    this.loadReports();
    this.loadUsers();
  }

  /* =========================
     CALCULATE REPORT TOTAL
     ========================= */

     private calculateReportTotal(report: any): number {
      if (!report.items || report.items.length === 0) return 0;
    
      const total = report.items.reduce((sum: number, item: any) => {
        return sum + Number(item.amount || 0);
      }, 0);
    
      // ✅ Fix floating precision
      return Number(total.toFixed(2));
    }
  /* =========================
     LOAD STATS
     ========================= */
  loadStats(): void {
    this.adminService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.createExpensesChart();
      },
      error: err => console.error('Stats error', err)
    });
  }

  /* =========================
     LOAD REPORTS
     ========================= */
  loadReports(): void {
    this.adminService.getReports().subscribe({
      next: (reports) => {

        this.reports = reports.map(r => ({
          ...r,
          calculatedTotal: this.calculateReportTotal(r)
        }));

        this.cdr.detectChanges();
      },
      error: err => console.error('Reports error', err)
    });
  }

  /* =========================
     EXPENSES CHART
     ========================= */
  createExpensesChart() {

    if (this.expensesChart) {
      this.expensesChart.destroy();
    }

    this.expensesChart = new Chart('expensesChart', {
      type: 'doughnut',
      data: {
        labels: ['Paid', 'Approved', 'Rejected'],
        datasets: [{
          data: [
            this.stats.paid,
            this.stats.approved,
            this.stats.rejected
          ],
          backgroundColor: [
            '#22c55e',
            '#3b82f6',
            '#ef4444'
          ],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });
  }

  /* =========================
     LOAD USERS
     ========================= */
  loadUsers(): void {
    this.userService.getAll().subscribe({
      next: (users: any[]) => {

        this.recentUsers = users.slice(0, 5);

        const counts = {
          EMPLOYEE: users.filter(u => u.role === 'EMPLOYEE').length,
          MANAGER: users.filter(u => u.role === 'MANAGER').length,
          ADMIN: users.filter(u => u.role === 'ADMIN').length,
        };

        this.createUsersChart(counts);
        this.cdr.detectChanges();
      },
      error: err => console.error(err)
    });
  }

  /* =========================
     USERS CHART
     ========================= */
  createUsersChart(counts: any) {

    if (this.usersChart) {
      this.usersChart.destroy();
    }

    this.usersChart = new Chart('usersChart', {
      type: 'bar',
      data: {
        labels: ['Employees', 'Managers', 'Admins'],
        datasets: [{
          data: [
            counts.EMPLOYEE,
            counts.MANAGER,
            counts.ADMIN
          ],
          backgroundColor: '#6366f1',
          borderRadius: 8
        }]
      },
      options: {
        plugins: { legend: { display: false } },
        scales: {
          y: {
            beginAtZero: true,
            ticks: { precision: 0 }
          }
        }
      }
    });
  }
}