import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';


import { ExpenseReportService } from '../../services/expense-report.service';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatProgressBarModule,
    MatChipsModule
  ],
  templateUrl: './reports.component.html'
})
export class ReportsComponent implements OnInit {

  reports: any[] = [];
  loading = false;
  userId!: number;

  constructor(private reportService: ExpenseReportService) {}

  ngOnInit(): void {
    const id = localStorage.getItem('userId');
    if (id) {
      this.userId = +id;
      this.loadReports();
    }
  }

  loadReports() {
    this.loading = true;
    this.reportService.getMyReports(this.userId).subscribe({
      next: (res) => {
        this.reports = res.content;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  createReport() {
    this.reportService.createReport(this.userId).subscribe(() => {
      this.loadReports();
    });
  }

  submitReport(reportId: number) {
    this.reportService.submitReport(reportId).subscribe(() => {
      this.loadReports();
    });
  }
}
