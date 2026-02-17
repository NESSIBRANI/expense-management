import { Expense } from './expense.model';

export type ExpenseStatus =
  | 'DRAFT'
  | 'SUBMITTED'
  | 'APPROVED'
  | 'REJECTED'
  | 'PAID';

export interface ExpenseReport {
  id: number;
  reference: string;
  createdAt: string;
  employeeId: number;
  employeeName: string;
  status: ExpenseStatus;
  items: Expense[];     // ⭐ CECI EST LE BON CHAMP BACKEND
  paidAt: string | null;
}
