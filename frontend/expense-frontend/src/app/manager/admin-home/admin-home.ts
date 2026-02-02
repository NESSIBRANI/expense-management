import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  templateUrl: './admin-home.html',
  styleUrls: ['./admin-home.css']
})
export class AdminHomeComponent {

  // 🔐 Manager connecté (mock)
  connectedManagerId = 1;

  expenses = [
    { amount: 120, status: 'PENDING', managerId: 1 },
    { amount: 300, status: 'APPROVED', managerId: 1 },
    { amount: 80, status: 'REJECTED', managerId: 2 },
    { amount: 200, status: 'PENDING', managerId: 1 },
    { amount: 150, status: 'APPROVED', managerId: 1 }
  ];

  // 📌 Expenses visibles du manager
  get managerExpenses() {
    return this.expenses.filter(e => e.managerId === this.connectedManagerId);
  }

  get totalExpenses() {
    return this.managerExpenses.length;
  }

  get pendingExpenses() {
    return this.managerExpenses.filter(e => e.status === 'PENDING').length;
  }

  get approvedExpenses() {
    return this.managerExpenses.filter(e => e.status === 'APPROVED').length;
  }

  get rejectedExpenses() {
    return this.managerExpenses.filter(e => e.status === 'REJECTED').length;
  }

  get totalAmount() {
    return this.managerExpenses.reduce((sum, e) => sum + e.amount, 0);
  }
}
