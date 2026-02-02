import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-admin-expenses',
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  templateUrl: './admin-expenses.html',
  styleUrls: ['./admin-expenses.css']
})
export class AdminExpensesComponent {

  /* =========================
     MOCK MANAGER CONNECTÉ
  ========================== */
  connectedManagerId = 1;

  /* =========================
     MOCK DATA
  ========================== */
  expenses = [
    { id: 1, employee: 'Ahmed Mohamed', amount: 120, date: '2025-01-10', status: 'PENDING', managerId: 1 },
    { id: 2, employee: 'Fatima Ali', amount: 300, date: '2025-01-12', status: 'PENDING', managerId: 1 },
    { id: 3, employee: 'Ali Salem', amount: 80, date: '2025-01-14', status: 'APPROVED', managerId: 2 }
  ];

  /* =========================
     MODAL STATE
  ========================== */
  selectedExpense: any = null;
  actionType: 'APPROVE' | 'REJECT' | null = null;

  /* =========================
     TOAST STATE
  ========================== */
  toastMessage: string = '';
  toastType: 'success' | 'error' | '' = '';

  /* =========================
     GETTERS
  ========================== */
  get managerExpenses() {
    return this.expenses.filter(e => e.managerId === this.connectedManagerId);
  }

  /* =========================
     MODAL ACTIONS
  ========================== */
  openModal(expense: any, action: 'APPROVE' | 'REJECT') {
    this.selectedExpense = expense;
    this.actionType = action;
  }

  confirmAction() {
    if (!this.selectedExpense || !this.actionType) return;

    if (this.actionType === 'APPROVE') {
      this.selectedExpense.status = 'APPROVED';
      this.showToast('Expense approved successfully', 'success');
    } else {
      this.selectedExpense.status = 'REJECTED';
      this.showToast('Expense rejected', 'error');
    }

    this.closeModal();
  }

  closeModal() {
    this.selectedExpense = null;
    this.actionType = null;
  }

  /* =========================
     TOAST
  ========================== */
  showToast(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;

    setTimeout(() => {
      this.toastMessage = '';
      this.toastType = '';
    }, 3000);
  }
}
