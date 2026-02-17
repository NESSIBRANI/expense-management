export interface ManagerExpense {
  id: number;
  title: string;
  amount: number;
  date: string;
}

export interface ManagerReport {
  id: number;
  reference: string;
  status: string;
  employeeName: string;
  expenses: ManagerExpense[];
}
