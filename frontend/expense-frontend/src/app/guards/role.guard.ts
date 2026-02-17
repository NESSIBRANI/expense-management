import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const RoleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {

  const auth = inject(AuthService);
  const router = inject(Router);

  const expectedRole = route.data['role'];

  // pas de token → login
  if (!auth.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const userRole = auth.getRole();

  // utilisateur pas encore chargé
  if (!userRole) {
    return true; // ⭐ IMPORTANT (empêche la déconnexion)
  }

  if (userRole !== expectedRole) {
    router.navigate(['/unauthorized']);
    return false;
  }

  return true;
};