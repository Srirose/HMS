import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';

export interface MenuItem {
  label: string;
  route?: string;
  roles: string[];
}

@Injectable({ providedIn: 'root' })
export class MenuService {
  private allItems: MenuItem[] = [
    { label: 'Dashboard', route: '/dashboard', roles: ['ADMIN', 'DOCTOR', 'NURSE'] },
    { label: 'Patients', route: '/patients', roles: ['ADMIN', 'NURSE'] },
    { label: 'Appointments', route: '/appointments', roles: ['ADMIN', 'DOCTOR'] },
    { label: 'Admin', route: '/admin', roles: ['ADMIN'] }
  ];

  constructor(private auth: AuthService) {}

  getMenuItems(): MenuItem[] {
    const roles = this.auth.getRoles();
    return this.allItems.filter(item => item.roles.some(r => roles.includes(r)));
  }
}


