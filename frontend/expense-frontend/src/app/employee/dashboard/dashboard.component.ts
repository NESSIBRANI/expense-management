import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

import { ExpenseReportService } from '../../services/expense-report.service';
import { ExpenseService } from '../../services/expense.service';
import { forkJoin } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatProgressBarModule,
    MatIconModule,
    MatChipsModule
  ],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements OnInit {

  loading = true;
  error = '';
  name = '';

  reports: any[] = [];
  expenses: any[] = [];

  reportStats = { total:0, draft:0, submitted:0, approved:0, rejected:0 };
  expenseStats = { total:0, draft:0, submitted:0, approved:0, rejected:0 };

  constructor(
    private router: Router,
    private reportService: ExpenseReportService,
    private expenseService: ExpenseService
  ) {}

  ngOnInit(): void {
    this.name = localStorage.getItem('name') || 'Employé';
    this.loadDashboard();
  }

  /* ================= LOAD DASHBOARD ================= */

  loadDashboard(): void {

  this.loading = true;
  this.error = '';

  forkJoin({
    reportsRes: this.reportService.getMyReports(),
    expensesRes: this.expenseService.getMyExpenses()
  }).subscribe({

    next: ({ reportsRes, expensesRes }: any) => {

      /* REPORTS */
      this.reports = reportsRes?.content ?? reportsRes ?? [];

      this.reportStats.total = this.reports.length;
      this.reportStats.draft = this.reports.filter(r => r.status === 'DRAFT').length;
      this.reportStats.submitted = this.reports.filter(r => r.status === 'SUBMITTED').length;
      this.reportStats.approved = this.reports.filter(r => r.status === 'APPROVED').length;
      this.reportStats.rejected = this.reports.filter(r => r.status === 'REJECTED').length;

      /* EXPENSES */
      this.expenses = expensesRes?.content ?? [];

      this.expenseStats.total = this.expenses.length;
      this.expenseStats.draft = this.expenses.filter(e => e.status === 'DRAFT').length;
      this.expenseStats.submitted = this.expenses.filter(e => e.status === 'SUBMITTED').length;
      this.expenseStats.approved = this.expenses.filter(e => e.status === 'APPROVED').length;
      this.expenseStats.rejected = this.expenses.filter(e => e.status === 'REJECTED').length;

      this.loading = false;
    },

    error: err => {
      console.error(err);
      this.error = "Impossible de charger votre tableau de bord";
      this.loading = false;
    }
  });
}

  /* ================= NAVIGATION ================= */

  goReports(){
    this.router.navigate(['/employee/reports']);
  }

  goExpenses(){
    this.router.navigate(['/employee/expenses']);
  }

  goReportDetails(id:number){
    this.router.navigate(['/employee/report', id]);
  }

  /* ⭐ OUVRIR AUTOMATIQUEMENT LE DRAFT */
  openDraftReport(){
    const draft = this.reports.find(r => r.status === 'DRAFT');
    if(draft){
      this.router.navigate(['/employee/report', draft.id]);
    }else{
      this.router.navigate(['/employee/reports']);
    }
  }


  
  /* LABEL STATUS */

  statusLabel(status: string): string {
    const map: any = {
      DRAFT: 'Brouillon',
      SUBMITTED: 'En attente manager',
      APPROVED: 'Approuvé',
      REJECTED: 'Rejeté',
      PAID: 'Payé'
    };
    return map[status] ?? status;
  }
}
