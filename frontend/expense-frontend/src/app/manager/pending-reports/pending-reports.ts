import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ManagerService } from '../../services/manager.service';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-pending-reports',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule
  ],
  templateUrl: './pending-reports.html',
  styleUrls: ['./pending-reports.css']
})
export class PendingReportsComponent implements OnInit {

  reports: any[] = [];
  loading = true;

  constructor(
    private api: ManagerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.load();
  }

  // ================= LOAD PENDING REPORTS =================
  load(){
    this.loading = true;

    this.api.getPendingReports().subscribe({
      next: (res:any) => {
        this.reports = res.content ?? res;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  // ================= OPEN DETAILS =================
  open(reportId: number){
    this.router.navigate(['/manager/report', reportId]);
  }

  // ================= APPROVE =================
approve(id: number) {
  this.api.approveReport(id).subscribe({
    next: () => this.load()
  });
}


  // ================= REJECT =================
  
reject(id: number) {

  const reason = prompt("Entrer la raison du rejet :");

  if (!reason || reason.trim().length === 0) {
    alert("La raison est obligatoire !");
    return;
  }

  this.api.rejectReport(id, reason).subscribe({
    next: () => {
      alert("Report rejeté !");
      this.load();
    },
    error: err => {
      console.error(err);
      alert("Erreur rejet");
    }
  });
}




}
