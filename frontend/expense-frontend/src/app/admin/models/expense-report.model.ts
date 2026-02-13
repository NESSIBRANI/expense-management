import { Expense } from './expense.model';

export interface ExpenseReport {
  id: number;
  reference: string;
  status: string;
  createdAt: string;
  employeeName: string;
  expenses: Expense[];
}
