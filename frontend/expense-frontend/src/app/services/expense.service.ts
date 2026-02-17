import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Expense } from '../models/expense.model';

export interface CreateExpenseRequest {
  title: string;
  amount: number;
  date: string; // yyyy-MM-dd
}

@Injectable({ providedIn: 'root' })
export class ExpenseService {

  private apiUrl = `${environment.apiUrl}/expenses`;
  private reportsApiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

   /* ================= EMPLOYEE ================= */

  // 🔹 Mes dépenses
  getMyExpenses(page: number = 0, size: number = 20): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/my?page=${page}&size=${size}`);
  }

  


  // 🔹 Ajouter une dépense
  addExpenseToReport(reportId: number, expense: CreateExpenseRequest): Observable<any> {
    return this.http.post<any>(
      `${this.reportsApiUrl}/${reportId}/expenses`,
      expense
    ).pipe(
      // Backward-compatible fallback for backend versions exposing /api/expenses/report/{reportId}
      catchError(() => this.http.post<any>(`${this.apiUrl}/report/${reportId}`, expense))
    );
  }

  // 🔹 MODIFIER UNE DÉPENSE
  updateExpense(expenseId: number, expense: any): Observable<any> {
    return this.http.put<any>(
      `${this.apiUrl}/${expenseId}`,
      expense
    );
  }

  // 🔹 SUPPRIMER UNE DÉPENSE  ←←← LA FONCTION MANQUANTE !!!
  deleteExpense(expenseId: number): Observable<any> {
    return this.http.delete<any>(
      `${this.apiUrl}/${expenseId}`
    );
  }



}
