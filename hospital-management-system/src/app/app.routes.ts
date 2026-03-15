import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardAdminComponent } from './components/dashboard-admin/dashboard-admin.component';
import { DashboardDoctorComponent } from './components/dashboard-doctor/dashboard-doctor.component';
import { DashboardNurseComponent } from './components/dashboard-nurse/dashboard-nurse.component';
import { PatientAdmissionComponent } from './components/patient-admission/patient-admission.component';
import { AppointmentCalendarComponent } from './components/appointment-calendar/appointment-calendar.component';
import { AnalyticsDashboardComponent } from './components/analytics-dashboard/analytics-dashboard.component';
import { AuditLogsComponent } from './components/audit-logs/audit-logs.component';
import { StaffManagementComponent } from './components/staff-management/staff-management.component';
import { PatientListComponent } from './components/patient-list/patient-list.component';
import { PatientDetailsComponent } from './components/patient-details/patient-details.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'admission', 
    component: PatientAdmissionComponent, 
    canActivate: [authGuard] 
  },
  {
    path: 'patients',
    component: PatientListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'patients/:id',
    component: PatientDetailsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'appointment-calendar',
    component: AppointmentCalendarComponent,
    canActivate: [authGuard]
  },
  {
    path: 'analytics-dashboard',
    component: AnalyticsDashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'audit-logs',
    component: AuditLogsComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'staff-management/:role',
    component: StaffManagementComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: 'ADMIN' }
  },
  { 
    path: 'admin', 
    component: DashboardAdminComponent, 
    canActivate: [authGuard, roleGuard], 
    data: { role: 'ADMIN' } 
  },
  { 
    path: 'doctor', 
    component: DashboardDoctorComponent, 
    canActivate: [authGuard, roleGuard], 
    data: { role: 'DOCTOR' } 
  },
  { 
    path: 'nurse', 
    component: DashboardNurseComponent, 
    canActivate: [authGuard, roleGuard], 
    data: { role: 'NURSE' } 
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
