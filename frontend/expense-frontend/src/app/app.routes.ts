import { Routes } from '@angular/router';
import { AdminLayout } from './admin/admin-layout/admin-layout';
import { AdminDashboard } from './admin/admin-dashboard/admin-dashboard';
import { AdminExpenses } from './admin/admin-expenses/admin-expenses';
import { AdminStats } from './admin/admin-stats/admin-stats';
import { AdminExports } from './admin/admin-exports/admin-exports';
import { Users } from './admin/users/users';
import { Login } from './login/login';
export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: 'admin',
    component: AdminLayout,
    children: [
      { path: '', component: AdminDashboard },
      { path: 'expenses', component: AdminExpenses ,  runGuardsAndResolvers: 'always'},
      { path: 'stats', component: AdminStats },
      { path: 'exports', component: AdminExports },
      { path: 'users', component: Users },
    ],
  },
  {
    path: '',
    redirectTo: 'admin',
    pathMatch: 'full',
  },
];
