import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  dob: string;
  gender: string;
  admissionDate?: string;
  assignedDoctorUsername?: string;
}

@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './patient-list.component.html',
  styleUrl: './patient-list.component.scss'
})
export class PatientListComponent implements OnInit {
  patients: Patient[] = [];
  filteredPatients: Patient[] = [];
  searchTerm: string = '';
  
  // Pagination
  page = 1;
  pageSize = 10;
  totalItems = 0;
  Math = Math;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    const tenant = localStorage.getItem('tenant') || 'hospital_a';
    const headers = new HttpHeaders().set('X-Tenant-ID', tenant);

    this.http.get<Patient[]>('http://localhost:8081/api/patients', { headers }).subscribe({
      next: (data) => {
        this.patients = data;
        this.applyFilter();
      },
      error: (err) => console.error('Failed to load patients', err)
    });
  }

  applyFilter(): void {
    if (!this.searchTerm) {
      this.filteredPatients = [...this.patients];
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredPatients = this.patients.filter(p => 
        p.firstName.toLowerCase().includes(term) || 
        p.lastName.toLowerCase().includes(term) ||
        p.id.toString().includes(term)
      );
    }
    this.totalItems = this.filteredPatients.length;
  }

  onSearch(): void {
    this.applyFilter();
    this.page = 1;
  }

  get paginatedPatients(): Patient[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filteredPatients.slice(start, start + this.pageSize);
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.pageSize);
  }

  nextPage(): void {
    if (this.page < this.totalPages) this.page++;
  }

  prevPage(): void {
    if (this.page > 1) this.page--;
  }
}
