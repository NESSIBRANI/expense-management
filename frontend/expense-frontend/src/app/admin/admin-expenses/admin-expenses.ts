import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface Report {
  id: number;
  reference: string;
  employeeName: string;
  status: string;
  createdAt: string;
}

@Component({
  selector: 'app-admin-expenses',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-expenses.html',
  styleUrls: ['./admin-expenses.css']
})
export class AdminExpenses implements OnInit {

  approvedReports: Report[] = [];
  otherReports: Report[] = [];

  loading = false;

  private apiUrl = 'http://localhost:8081/api/admin/reports';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadReports();
  }

  loadReports() {
    this.loading = true;
  
    // 1️⃣ Load approved
    this.http
      .get<{ content: Report[] }>(`${this.apiUrl}/approved?page=0&size=50`)
      .subscribe({
        next: (res) => {
          this.approvedReports = res.content || [];
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error loading approved reports:', err);
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  
    // 2️⃣ Load paid (or other)
    this.http
      .get<{ content: Report[] }>(`${this.apiUrl}/paid?page=0&size=50`)
      .subscribe({
        next: (res) => {
          this.otherReports = res.content || [];
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error loading paid reports:', err);
          this.cdr.detectChanges();
        }
      });
  }

  pay(id: number) {
    if (!confirm('Confirm payment?')) return;

    this.http
      .put(`${this.apiUrl}/${id}/pay`, {})
      .subscribe({
        next: () => this.loadReports(),
        error: (err) => console.error('Payment failed:', err)
      });
  }
  exportReports() {
    const url = 'http://localhost:8081/api/admin/export/excel/employees';
  
    this.http.get(url, {
      responseType: 'blob'
    }).subscribe({
      next: (blob) => {
  
        const a = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
  
        a.href = objectUrl;
        a.download = 'expenses.xlsx';
        a.click();
  
        URL.revokeObjectURL(objectUrl);
      },
      error: (err) => {
        console.error('Export failed:', err);
      }
    });
  }
  
}