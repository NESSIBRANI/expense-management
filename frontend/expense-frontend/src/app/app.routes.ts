import { Routes } from '@angular/router';

import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';

import { DashboardComponent } from './employee/dashboard/dashboard.component';
import { AddExpenseComponent } from './employee/add-expenses/add-expense.component';
import { ExpensesComponent } from './employee/expenses/expenses.component';
import { ReportsComponent } from './employee/reports/reports.component';

import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';
import { UnauthorizedComponent } from './shared/unauthorized/unauthorized';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  {
    path: 'employee',
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'EMPLOYEE' },
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'reports', component: ReportsComponent },
      { path: 'add-expense/:reportId', component: AddExpenseComponent },
      { path: 'expenses', component: ExpensesComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  {
    path: 'manager',
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'MANAGER' },
    loadComponent: () =>
      import('./manager-dashboard/manager-dashboard')
        .then(m => m.ManagerDashboardComponent)
  },

  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: '**', redirectTo: 'login' }
];
