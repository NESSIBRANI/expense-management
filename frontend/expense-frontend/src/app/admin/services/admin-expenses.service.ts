import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdminExpensesService {

  private baseUrl = 'http://localhost:8081/api/reports';

  constructor(private http: HttpClient) {}

  // ✅ EXISTING BACKEND ENDPOINT
  getSubmittedReports(): Observable<any> {
    return this.http.get(`${this.baseUrl}/pending`);
  }
}
