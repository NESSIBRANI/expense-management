import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagerService } from '../../services/manager.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './manager-dashboard.html',
  styleUrls: ['./manager-dashboard.css']
})
export class ManagerDashboardComponent implements OnInit {

  // reports
  reports: any[] = [];

  // stats
  pendingCount = 0;
  approvedCount = 0;
  rejectedCount = 0;
  paidCount = 0;
  totalCount = 0;


  constructor(
  private managerService: ManagerService,
  private router: Router
) {}


  ngOnInit(): void {
    this.loadStats();
    this.loadReports();
  }

  // =========================
  // LOAD STATS
  // =========================
 loadStats() {
  this.managerService.getStats().subscribe({
    next: (stats: any) => {

      this.pendingCount = stats.pending;
      this.approvedCount = stats.approved;
      this.rejectedCount = stats.rejected;
      this.paidCount = stats.paid;

      // ⭐⭐ TOTAL CORRECT METIER
      this.totalCount =
        stats.approved +
        stats.rejected +
        stats.paid;
    },
    error: err => console.error(err)
  });
}



  // =========================
  // LOAD PENDING REPORTS
  // =========================
  loadReports() {
  this.managerService.getPendingReports().subscribe({
    next: (res: any) => {

      // ⭐⭐⭐ IMPORTANT : force Angular refresh
      this.reports = [...(res.content || [])];

    },
    error: err => console.error(err)
  });
}


goToPending(){
  this.router.navigate(['/manager/pending']);
}



 
}
