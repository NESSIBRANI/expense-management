import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';


import { ExpenseReportService } from '../../services/expense-report.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule
  ],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent {

  constructor(
    private router: Router,
    private reportService: ExpenseReportService
  ) {}

  onAddExpense() {
    const userId = localStorage.getItem('userId');
    if (!userId) return;

    this.reportService.createReport(+userId).subscribe({
      next: (report) => {
        this.router.navigate(['/employee/add-expense', report.id]);
      }
    });
  }

  goReports() {
    this.router.navigate(['/employee/reports']);
  }

  goExpenses() {
    this.router.navigate(['/employee/expenses']);
  }
}
