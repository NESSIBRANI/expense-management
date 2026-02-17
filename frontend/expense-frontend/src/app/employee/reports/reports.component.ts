import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthStateService } from '../../services/auth-state.service';


import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { NotifyService } from '../../services/notify.service';
import { filter, take } from 'rxjs/operators';
import { ExpenseReportService } from '../../services/expense-report.service';
import { ExpenseReport } from '../../models/expense-report.model';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatProgressBarModule,
    MatChipsModule,
    MatIconModule
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.css']
})
export class ReportsComponent implements OnInit {

reports: ExpenseReport[] = [];
loading = true;
userId!: number;
error = '';


  constructor(
  private reportService: ExpenseReportService,
  private router: Router,
  private notify: NotifyService,
  private authState: AuthStateService
) {}


ngOnInit(): void {
  // ✅ Cas refresh / accès direct : userId déjà en localStorage
  const id = localStorage.getItem('userId');
  if (id) {
    this.userId = +id;
    this.loadReports();
    return;
  }

  // ✅ Cas login juste maintenant : attendre que AuthState devienne ready
  this.authState.userReady$
    .pipe(filter(Boolean), take(1))
    .subscribe(() => {
      const newId = localStorage.getItem('userId');
      if (newId) {
        this.userId = +newId;
        this.loadReports();
      }
    });
}


  /* ================= LOAD REPORTS ================= */
loadReports(): void {

  this.loading = true;
  this.error = '';

  this.reportService.getMyReports().subscribe({
    next: (res: any) => {

      let reports: ExpenseReport[] = [];

      if (res?.content) {
        reports = res.content;
      } else if (Array.isArray(res)) {
        reports = res;
      }

      // 🔴 DETECTION APPROVAL / REJECTION
      reports.forEach((r: any) => {

        const key = "report_seen_" + r.id;

        // APPROVED
        if (r.status === 'APPROVED' && !localStorage.getItem(key)) {

          this.notify.success("Votre note de frais a été approuvée ✔");

          localStorage.setItem(key, "1");
        }

        // REJECTED
        if (r.status === 'REJECTED' && !localStorage.getItem(key)) {

          this.notify.error("Votre note de frais a été rejetée ❌");

          if (r.managerComment) {
            setTimeout(() => {
              this.notify.info("Motif : " + r.managerComment);
            }, 900);
          }

          localStorage.setItem(key, "1");
        }

      });

      this.reports = reports;
      this.loading = false;
    },

    error: (err) => {
      console.error(err);
      this.error = 'Impossible de charger les rapports';
      this.loading = false;
    }
  });
}

  /* ================= CREATE REPORT ================= */

  createReport(): void {
    this.loading = true;

    this.reportService.createReport().subscribe({
      next: (report: ExpenseReport) => {

        if (!report || !report.id) {
          this.error = 'Erreur création du report';
          this.loading = false;
          return;
        }

        // ouvrir directement la page détail
        this.router.navigate(['/employee/report', report.id]);
      },
      error: (err) => {
        console.error(err);
        this.error = 'Impossible de créer la note de frais';
        this.loading = false;
      }
    });
  }

  /* ================= NAVIGATION ================= */

  goToDetails(reportId: number): void {
    this.router.navigate(['/employee/report', reportId]);
  }

  /* ================= DELETE ================= */

  deleteReport(id: number): void {

  if (!confirm("Supprimer cette note de frais ?")) return;

  this.reportService.deleteReport(id).subscribe({
    next: () => {

      // notification moderne
      this.notify.success("Note de frais supprimée ✔");

      // reload
      this.loadReports();
    },

    error: (err) => {
      console.error(err);
      this.notify.error("Impossible : la note n'est plus en brouillon");
    }
  });
}


  /* ================= STATUS LABEL ================= */

  statusLabel(status: string): string {

    const labels: any = {
      DRAFT: 'Brouillon',
      SUBMITTED: 'Soumis',
      APPROVED: 'Approuvé',
      REJECTED: 'Rejeté',
      PAID: 'Remboursé'
    };

    return labels[status] ?? 'Inconnu';
  }



  
}
