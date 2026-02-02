import { Routes } from '@angular/router';

// Auth
import { LoginComponent } from './auth/login/login';

// Dashboards
import { EmployeeDashboardComponent } from './employee/employee-dashboard/employee-dashboard';
import { ManagerDashboardComponent } from './manager/manager-dashboard/manager-dashboard';

// Guards
import { AuthGuard } from './guards/auth-guard';
import { RoleGuard } from './guards/role-guard';

// Manager / Admin pages
import { AdminHomeComponent } from './manager/admin-home/admin-home';
import { AdminEmployeesComponent } from './manager/admin-employees/admin-employees';
import { AdminExpensesComponent } from './manager/admin-expenses/admin-expenses';
import { AdminReportsComponent } from './manager/admin-reports/admin-reports';

export const routes: Routes = [

  // Redirect root → login
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Login
  { path: 'login', component: LoginComponent },

  // Employee dashboard
  {
    path: 'employee',
    component: EmployeeDashboardComponent,
   // canActivate: [AuthGuard, RoleGuard],
   // data: { role: 'EMPLOYEE' }
  },

  // Manager / Admin dashboard
  {
    path: 'manager',
    component: ManagerDashboardComponent,
   // canActivate: [AuthGuard, RoleGuard],
   // data: { role: 'MANAGER' },
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: AdminHomeComponent },
      { path: 'employees', component: AdminEmployeesComponent },
      { path: 'expenses', component: AdminExpensesComponent },
      { path: 'reports', component: AdminReportsComponent },
    ]
  }

];
