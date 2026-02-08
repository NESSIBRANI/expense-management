import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ExpenseReportService {
  private api = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

  getMyReports(userId: number, page = 0, size = 5): Observable<any> {
    return this.http.get<any>(`${this.api}/my`, {
      params: { userId, page, size }
    });
  }

  createReport(userId: number): Observable<any> {
    return this.http.post<any>(`${this.api}`, {}, { params: { userId } });
  }

  submitReport(reportId: number): Observable<any> {
    return this.http.put<any>(`${this.api}/${reportId}/submit`, {});
  }
}
