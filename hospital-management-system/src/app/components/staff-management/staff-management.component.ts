import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-staff-management',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './staff-management.component.html',
  styleUrls: ['./staff-management.component.scss']
})
export class StaffManagementComponent implements OnInit {
  staffList: any[] = [];
  role: string = '';

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.role = params['role'];
      this.loadStaff();
    });
  }

  loadStaff(): void {
    const tenant = localStorage.getItem('tenant') || 'hospital_a';
    const headers = new HttpHeaders().set('X-Tenant-ID', tenant);
    const url = `http://localhost:8081/api/admin/${this.role}s`; // e.g., doctors or nurses

    this.http.get<any[]>(url, { headers }).subscribe({
      next: (data) => {
        console.log(`Loaded ${this.role}s:`, data);
        this.staffList = data;
      },
      error: (err) => console.error(`Failed to load ${this.role}s`, err)
    });
  }

  deleteStaff(id: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      const tenant = localStorage.getItem('tenant') || 'hospital_a';
      const headers = new HttpHeaders().set('X-Tenant-ID', tenant);
      
      this.http.delete(`http://localhost:8081/api/admin/users/${id}`, { headers }).subscribe({
        next: () => this.loadStaff(),
        error: (err) => console.error('Failed to delete user', err)
      });
    }
  }
}
