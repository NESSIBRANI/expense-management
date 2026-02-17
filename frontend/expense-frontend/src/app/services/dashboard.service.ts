import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DashboardService {

  private api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // REPORTS
  getMyReports(): Observable<any> {
    return this.http.get(`${this.api}/reports/my`);
  }

  // EXPENSES
  getMyExpenses(): Observable<any> {
    return this.http.get(`${this.api}/expenses/my`);
  }
}
