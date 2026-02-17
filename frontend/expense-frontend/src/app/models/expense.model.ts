export type ExpenseStatus =
  | 'DRAFT'
  | 'SUBMITTED'
  | 'APPROVED'
  | 'REJECTED'
  | 'PAID';

export interface Expense {
  id: number;
  title: string;
  amount: number;
  date: string;
  status: ExpenseStatus;
  createdAt?: string;
  managerComment?: string | null;
  userId?: number;
  reportId?: number;
}
