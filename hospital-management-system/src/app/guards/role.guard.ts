import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const expectedRole = route.data['role'];

  const currentRole = authService.getUserRole();

  if (authService.isLoggedIn() && currentRole === expectedRole) {
    return true;
  }

  // If logged in but wrong role, maybe redirect to their dashboard? 
  // For simplicity, we just return false and let them handle it or redirect to login
  router.navigate(['/login']);
  return false;
};
