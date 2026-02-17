import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

/* Angular Material */
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { ChangeDetectorRef } from '@angular/core';

import { ExpenseReport } from '../../models/expense-report.model';
import { CreateExpenseRequest, ExpenseService } from '../../services/expense.service';
import { ExpenseReportService } from '../../services/expense-report.service';
import { Expense } from '../../models/expense.model';
import { NotifyService } from '../../services/notify.service';

@Component({
  selector: 'app-report-details',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatDividerModule

  ],
  templateUrl: './report-details.html',
  styleUrls: ['./report-details.css']
})
export class ReportDetailsComponent implements OnInit {

  reportId!: number;
  report?: ExpenseReport;
 expenses: Expense[] = [];
  loadingReport = true;
  loading = true;

  /* ===== FORM ===== */
  showForm = false;
  description = '';
  amount: number | null = null;
  expenseDate: Date | null = null;
  
constructor(
  private route: ActivatedRoute,
  private expenseService: ExpenseService,
  private reportService: ExpenseReportService,
  private router: Router,
  private cdr: ChangeDetectorRef,
   private notify: NotifyService
) {}


ngOnInit(): void {

  this.route.paramMap.subscribe(params => {

    const id = Number(params.get('id'));

    if (!id) {
      console.error("ID report invalide !");
      return;
    }

    // 🔥 reset avant chargement
    this.report = undefined;
    this.expenses = [];
    this.loading = true;

    this.reportId = id;

    console.log("Chargement du report :", this.reportId);

    this.loadReport();

  });
}



  /* ================= LOAD REPORT ================= */
loadReport(): void {

  this.loading = true;

  this.reportService.getMyReport(this.reportId).subscribe({

    next: (report: ExpenseReport) => {

      console.log("REPORT =", report);

      // ⭐⭐⭐ LA CORRECTION CRITIQUE
      this.report = { ...report };

      this.expenses = report.items ?? [];



      this.loading = false;

      // ⭐⭐⭐ OBLIGATOIRE
      this.cdr.detectChanges();

    },

    error: (err) => {
      console.error("Erreur chargement report :", err);
      this.loading = false;
    }
  });
}





  /* ================= DATE FORMAT ================= */

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  /* ================= TOGGLE FORM ================= */

  toggleForm() {
    if (!this.isDraft) return;
    this.showForm = !this.showForm;
  }

  /* ================= ADD EXPENSE ================= */

  addExpense() {

    if (!this.description || this.amount === null || !this.expenseDate) {
      alert("Veuillez remplir tous les champs");
      return;
    }

    const payload: CreateExpenseRequest = {
      title: this.description.trim(),
      amount: Number(this.amount),
      date: this.formatDate(this.expenseDate)
    };

    this.expenseService.addExpenseToReport(this.reportId, payload)
      .subscribe({
        next: () => {

          // reset
          this.description = '';
          this.amount = null;
          this.expenseDate = null;
          this.showForm = false;

          // reload
          this.loadReport();
        },
        error: err => {
          console.error(err);
          alert("Erreur lors de l'ajout");
        }
      });
  }

  /* ================= DELETE ================= */

  deleteExpense(id: number) {
    if (!confirm("Supprimer cette dépense ?")) return;

    this.expenseService.deleteExpense(id)
      .subscribe(() => this.loadReport());
  }

  /* ================= SUBMIT REPORT ================= */

 submitReport() {

  if (!this.report || this.expenses.length === 0) {
    this.notify.error("Ajoutez au moins une dépense avant de soumettre !");
    return;
  }

  this.loading = true;

  this.reportService.submitReport(this.report.id)
    .subscribe({
      next: () => {

        // Mise à jour locale du statut
        this.report = {
          ...this.report!,
          status: 'SUBMITTED'
        };

        this.cdr.detectChanges();

        // ⭐⭐ ICI LA NOUVELLE NOTIFICATION (comme manager)
        this.notify.info("Votre note de frais a été envoyée au manager 📤");

        // redirection après affichage
        setTimeout(() => {
          this.router.navigate(['/employee/reports']);
        }, 1300);
      },

      error: err => {
        console.error(err);
        this.loading = false;
        this.notify.error("Impossible d'envoyer la note de frais");
      }
    });
}



  /* ================= HELPERS ================= */

 get isDraft(): boolean {
  return this.report?.status === 'DRAFT';
}



  goBack() {
    this.router.navigate(['/employee/reports']);
  }
}

