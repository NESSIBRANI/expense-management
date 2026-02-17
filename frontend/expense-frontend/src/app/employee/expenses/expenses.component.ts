import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ExpenseReportService } from '../../services/expense-report.service';


import { ExpenseService } from '../../services/expense.service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-expenses',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatSelectModule,
    MatChipsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatProgressBarModule
  ],
  templateUrl: './expenses.html',
  styleUrls: ['./expenses.css']
})
export class ExpensesComponent implements OnInit {

  displayedColumns: string[] = ['title', 'amount', 'date', 'status', 'report', 'actions'];
  dataSource = new MatTableDataSource<any>([]);

  loading = true;
  error = '';
  statusFilter = 'ALL';

  constructor(
    private expenseService: ExpenseService,
    private reportService: ExpenseReportService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadExpenses();
  }

  // ⭐⭐⭐ CORRECTION MAJEURE ICI
  loadExpenses(): void {

  this.loading = true;
  this.error = '';

  // 1️⃣ récupérer tous les reports
  this.reportService.getMyReports(0, 50).subscribe({

    next: (res) => {

      const reports = res?.content ?? [];
      const allExpenses: any[] = [];

      // 2️⃣ pour chaque report → récupérer ses dépenses
      reports.forEach((report: any) => {

        const reportId = report.id;

        (report.items ?? []).forEach((exp: any) => {

  allExpenses.push({
    ...exp,
    reportId: reportId,

    // ⭐⭐⭐ LA CORRECTION PRINCIPALE
    reportStatus: report.status,

    date: exp.date ? new Date(exp.date) : null
  });

});


      });

      // 3️⃣ injecter dans la table
      this.dataSource.data = allExpenses;

      this.applyFilter();
      this.loading = false;

    },

    error: (err) => {
      console.error(err);
      this.error = 'Impossible de charger les dépenses';
      this.loading = false;
    }
  });
}


  // filtre statut
  applyFilter(): void {
    this.dataSource.filterPredicate = (data: any) => {
      if (this.statusFilter === 'ALL') return true;
      return data.status === this.statusFilter;
    };

    this.dataSource.filter = Math.random().toString();
  }

  // ouvrir le report
 goToReport(expense: any): void {

  // récupérer l'id du report correctement
  let reportId: number | null = null;

  if (expense.expenseReport && expense.expenseReport.id) {
    reportId = expense.expenseReport.id;
  }
  else if (expense.report && expense.report.id) {
    reportId = expense.report.id;
  }
  else if (expense.reportId) {
    reportId = expense.reportId;
  }

  if (!reportId) {
    console.error("Aucun report lié à cette dépense :", expense);
    return;
  }

  console.log("Ouverture du report ID =", reportId);

  this.router.navigate(['/employee/report', reportId]);
}


  // affichage texte statut
 statusLabel(status: string): string {

  const map: any = {
    DRAFT: 'Brouillon',
    SUBMITTED: 'Soumise',
    APPROVED: 'Approuvée',
    REJECTED: 'Rejetée',
    PAID: 'Remboursée'
  };

  return map[status] ?? status;
}


  // couleur du badge
  statusColor(status: string): string {
    const map: any = {
      DRAFT: 'basic',
      PENDING: 'primary',
      APPROVED: 'accent',
      REJECTED: 'warn'
    };
    return map[status] ?? 'basic';
  }
}
