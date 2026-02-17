import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReportService {

  private apiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

  // mes reports
  getMyReports(page: number = 0, size: number = 20): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/my?page=${page}&size=${size}`);
  }

  // créer report
  createReport(): Observable<any> {
    return this.http.post(`${this.apiUrl}`, {});
  }

  // soumettre report
  submitReport(reportId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${reportId}/submit`, {});
  }

  // détails report
  getReport(reportId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${reportId}`);
  }
}
