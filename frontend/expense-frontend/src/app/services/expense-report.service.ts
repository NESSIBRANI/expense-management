import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ExpenseReport } from '../models/expense-report.model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ExpenseReportService {

  private apiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

  /* =========================
     📄 GET MY REPORTS
     ========================= */
  getMyReports(page: number = 0, size: number = 20)
    : Observable<{ content: ExpenseReport[] }> {

    return this.http.get<{ content: ExpenseReport[] }>(
      `${this.apiUrl}/my?page=${page}&size=${size}`
    );
  }

  /* =========================
     ➕ CREATE REPORT
     ========================= */
  createReport(): Observable<ExpenseReport> {
    return this.http.post<ExpenseReport>(this.apiUrl, {});
  }
/* =========================
   📤 SUBMIT REPORT
   ========================= */
submitReport(reportId: number): Observable<ExpenseReport> {
  return this.http.put<ExpenseReport>(
    `${this.apiUrl}/${reportId}/submit`,
    {}
  );
}


/* =========================
   🔍 GET ONE REPORT BY ID
   ========================= */

getMyReport(reportId: number): Observable<ExpenseReport> {
  return this.http.get<ExpenseReport>(`${this.apiUrl}/my/${reportId}`);
}

deleteReport(id:number){
  return this.http.delete(`${this.apiUrl}/${id}`);
}





}
