import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExpenseService } from '../services/expense.service';

import { MaterialModule } from '../material.module';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MaterialModule   // ✅ BIEN ICI
  ],
  templateUrl: './manager-dashboard.html',
  styleUrls: ['./manager-dashboard.css']
})
export class ManagerDashboardComponent implements OnInit {

  expenses: any[] = [];

  constructor(private expenseService: ExpenseService) {}

  ngOnInit(): void {
    this.loadExpenses();
  }

  // 👀 Charger toutes les notes
  loadExpenses() {
    this.expenseService.getAllExpenses().subscribe({
      next: (data: any[]) => this.expenses = data,
      error: (err: any) => console.error(err)
    });
  }

  // ✅ Valider
  approve(id: number) {
    this.expenseService.approveExpense(id).subscribe(() => {
      this.loadExpenses();
    });
  }

  // ❌ Rejeter
  reject(id: number) {
    this.expenseService.rejectExpense(id).subscribe(() => {
      this.loadExpenses();
    });
  }
}
