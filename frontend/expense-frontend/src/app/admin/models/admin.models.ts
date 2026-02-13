export interface MonthlyTotal {
    month: string;
    total: number;
  }
  
  export interface EmployeeTotal {
    employeeId: number;
    employeeName: string;
    total: number;
  }
  
  export interface ExpenseResponse {
    id: number;
    title: string;
    amount: number;
    date: string;
    status: string;
    createdAt: string;
    managerComment?: string | null;
    userId: number;
  }
  
  export interface ExpenseReportResponse {
    id: number;
    reference: string;
    createdAt: string;
    employeeId: number;
    employeeName: string; 
    items: ExpenseResponse[];
    paidAt?: string | null;
  }
  