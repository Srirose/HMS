import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

interface PatientSummary {
  id: number;
  firstName: string;
  lastName: string;
  dob: string;
  gender: string;
  admissionDetails: any;
}

@Component({
  selector: 'app-dashboard-doctor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-doctor.component.html',
  styleUrl: './dashboard-doctor.component.scss'
})
export class DashboardDoctorComponent implements OnInit {
  myAdmissions: PatientSummary[] = [];

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadMyAdmissions();
  }

  private loadMyAdmissions(): void {
    const tenant = localStorage.getItem('tenant') || 'hospital_a';
    const headers = new HttpHeaders().set('X-Tenant-ID', tenant);

    this.http
      .get<PatientSummary[]>('http://localhost:8081/api/doctor/admissions', { headers })
      .subscribe({
        next: patients => (this.myAdmissions = patients),
        error: err => console.error('Failed to load admissions', err)
      });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
