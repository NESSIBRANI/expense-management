import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {

  private apiUrl = 'http://localhost:8081/api/expenses';

  constructor(private http: HttpClient) {}

  // =========================
  // EMPLOYEE
  // =========================
  getMyExpenses(page = 0, size = 5): Observable<any> {
    const userId = localStorage.getItem('userId');
    return this.http.get<any>(`${this.apiUrl}/my`, {
      params: { userId: userId!, page, size }
    });
  }

  createExpense(reportId: number, expense: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/report/${reportId}`, expense);
  }

  // =========================
  // MANAGER
  // =========================
  getAllExpenses(): Observable<any> {
    return this.http.get(`${this.apiUrl}/all`);
  }

  approveExpense(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/approve`, {});
  }

  rejectExpense(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/reject`, {});
  }
}
