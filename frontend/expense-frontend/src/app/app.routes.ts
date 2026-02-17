import { Routes } from '@angular/router';

import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';

import { EmployeeLayoutComponent } from './employee/employee-layout/employee-layout.component';

import { DashboardComponent } from './employee/dashboard/dashboard.component';
import { ExpensesComponent } from './employee/expenses/expenses.component';
import { ReportsComponent } from './employee/reports/reports.component';
import { ReportDetailsComponent } from './employee/report-details/report-details.component';

import { ManagerLayoutComponent } from './manager/layout/manager-layout';
import { ManagerDashboardComponent } from './manager/manager-dashboard/manager-dashboard';
import { PendingReportsComponent } from './manager/pending-reports/pending-reports';

import { AdminLayout } from './admin/admin-layout/admin-layout';


import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';
import { UnauthorizedComponent } from './shared/unauthorized/unauthorized';

export const routes: Routes = [

  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // AUTH
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // ================= EMPLOYEE =================
  {
    path: 'employee',
    component: EmployeeLayoutComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'EMPLOYEE' },

    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'reports', component: ReportsComponent },
      { path: 'report/:id', component: ReportDetailsComponent },
    
      { path: 'expenses', component: ExpensesComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // ================= MANAGER =================

{
  path: 'manager',
  component: ManagerLayoutComponent,
  canActivate: [AuthGuard, RoleGuard],
  data: { role: 'MANAGER' },

  children: [
    { path: 'dashboard', component: ManagerDashboardComponent },
    { path: 'pending', component: PendingReportsComponent },

    // ⭐ ROUTE REPORT DETAILS (CORRECTE)
    {
      path: 'report/:id',
      loadComponent: () =>
        import('./manager/manager-report-details/manager-report-details')
          .then(c => c.ManagerReportDetailsComponent)
    },

    { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
  ]
},

// =========================
  // ADMIN AREA
  // =========================
   {
  path: 'admin',
  component: AdminLayout,
  canActivate: [AuthGuard, RoleGuard],
  data: { role: 'ADMIN' },

  children: [
    {
      path: '',
      loadComponent: () =>
        import('./admin/admin-dashboard/admin-dashboard').then(m => m.AdminDashboard)
    },
    {
      path: 'expenses',
      loadComponent: () =>
        import('./admin/admin-expenses/admin-expenses').then(m => m.AdminExpenses)
    },
    {
      path: 'users',
      loadComponent: () =>
        import('./admin/users/users').then(m => m.UsersComponent)
    },
    {
      path: 'exports',
      loadComponent: () =>
        import('./admin/admin-exports/admin-exports').then(m => m.AdminExports)
    },

      {
       path: 'stats',
       loadComponent: () =>
        import('./admin/admin-stats/admin-stats').then(m => m.AdminStats)
}

  ]
},


  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: '**', redirectTo: 'login' }
];