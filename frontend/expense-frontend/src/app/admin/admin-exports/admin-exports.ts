import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs/operators';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-admin-exports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-exports.html',
  styleUrls: ['./admin-exports.css']
})
export class AdminExports implements OnInit {

  isExportingEmployee = false;
  isExportingMonth = false;

  private baseUrl = 'http://localhost:8081/api/admin';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {}

  // ============================
  // EXPORT: Paid Expenses by Employee
  // ============================
  exportByEmployee() {

    this.isExportingEmployee = true;
    this.cdr.detectChanges();

    this.http.get(
      `${this.baseUrl}/export/excel/employees`,
      { responseType: 'blob' }
    )
    .pipe(
      finalize(() => {
        this.isExportingEmployee = false;
        this.cdr.detectChanges();
      })
    )
    .subscribe({
      next: (blob) => {
        this.downloadFile(blob, 'paid_expenses_by_employee.xlsx');
      },
      error: (err) => {
        console.error('Employee export failed', err);
      }
    });
  }

  // ============================
  // EXPORT: Paid Expenses by Month
  // ============================
  exportByMonth() {

    this.isExportingMonth = true;
    this.cdr.detectChanges();

    this.http.get<any[]>(
      `${this.baseUrl}/stats/monthly`
    )
    .pipe(
      finalize(() => {
        this.isExportingMonth = false;
        this.cdr.detectChanges();
      })
    )
    .subscribe({
      next: (data) => {

        const worksheet = XLSX.utils.json_to_sheet(
          data.map(d => ({
            Month: d.month,
            'Total Paid': d.total
          }))
        );

        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, 'Monthly Summary');

        XLSX.writeFile(workbook, 'paid_expenses_by_month.xlsx');
      },
      error: (err) => {
        console.error('Monthly export failed', err);
      }
    });
  }

  // ============================
  // FILE DOWNLOAD HELPER
  // ============================
  private downloadFile(blob: Blob, filename: string) {
    const a = document.createElement('a');
    const objectUrl = URL.createObjectURL(blob);

    a.href = objectUrl;
    a.download = filename;
    a.click();

    URL.revokeObjectURL(objectUrl);
  }
}