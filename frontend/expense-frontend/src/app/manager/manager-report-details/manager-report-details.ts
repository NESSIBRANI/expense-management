import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { ManagerService } from '../../services/manager.service';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';

import { MatDialog } from '@angular/material/dialog';
import { RejectDialogComponent } from '../../reject-dialog/reject-dialog';
import { NotifyService } from '../../services/notify.service';

@Component({
  selector: 'app-manager-report-details',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  templateUrl: './manager-report-details.html',
  styleUrls: ['./manager-report-details.css']
})
export class ManagerReportDetailsComponent implements OnInit {

  reportId!: number;
  report: any = null;
  expenses: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private api: ManagerService,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog,
    private notify: NotifyService
  ) {}

  // ================= INIT =================
  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {

      const id = Number(params.get('id'));
      if (!id) return;

      this.reportId = id;

      this.report = null;
      this.expenses = [];
      this.error = null;

      this.load();
    });
  }

  // ================= LOAD REPORT =================
  load(): void {

    this.loading = true;

    this.api.getReportDetails(this.reportId).subscribe({
      next: (res: any) => {

        this.report = { ...res };
        this.expenses = [...(res?.items ?? res?.expenses ?? [])];

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.error = err?.error?.message || 'Erreur chargement report';
        this.loading = false;
        this.notify.error("Impossible de charger le report");
        this.cdr.detectChanges();
      }
    });
  }

  // ================= BACK =================
  goBack() {
    this.router.navigate(['/manager/pending']);
  }

  // ================= APPROVE =================
  approve() {

    if (this.report?.status !== 'SUBMITTED') return;

    this.loading = true;

    this.api.approveReport(this.reportId).subscribe({
      next: () => {

        this.notify.success("Note de frais approuvée ✔");

        // retour vers la liste
        this.router.navigate(['/manager/pending']);
      },
      error: err => {
        console.error(err);
        this.loading = false;
        this.notify.error("Impossible d'approuver la note de frais");
      }
    });
  }

  // ================= REJECT =================
  reject() {

    if (this.report?.status !== 'SUBMITTED') return;

    const dialogRef = this.dialog.open(RejectDialogComponent, {
      width: '520px',
      maxWidth: 'calc(100vw - 32px)',
      autoFocus: false,
      disableClose: true,
      backdropClass: 'reject-backdrop',
      data: { reportId: this.report.id }
    });

    dialogRef.afterClosed().subscribe((reason: string) => {

      if (!reason) return;

      this.loading = true;

      this.api.rejectReport(this.reportId, reason)
        .subscribe({
          next: () => {

            this.notify.error("Note de frais rejetée ❌");

            // recharge le report (il devient REJECTED)
            this.load();
          },
          error: (err) => {
            console.error(err);
            this.loading = false;
            this.notify.error("Erreur lors du rejet");
          }
        });

    });
  }
}
