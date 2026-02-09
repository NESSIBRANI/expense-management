import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material.module';
import { ExpenseService } from '../../services/expense.service';

import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-expenses',
  standalone: true,
  imports: [CommonModule, MaterialModule],
  templateUrl: './expenses.html',
  styleUrls: ['./expenses.css']
})
export class ExpensesComponent implements OnInit {
  displayedColumns = ['title', 'amount', 'status'];
  dataSource = new MatTableDataSource<any>([]);

  constructor(private expenseService: ExpenseService) {}

  ngOnInit(): void {
    this.expenseService.getMyExpenses(0, 10).subscribe({
      next: (res) => (this.dataSource.data = res.content ?? []),
      error: (err) => console.error(err)
    });
  }
}
