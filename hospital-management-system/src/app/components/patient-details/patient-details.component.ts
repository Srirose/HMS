import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, RouterModule } from '@angular/router';

import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-patient-details',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './patient-details.component.html',
  styleUrl: './patient-details.component.scss'
})
export class PatientDetailsComponent implements OnInit {
  patient: any = null;
  loading = true;
  error = '';
  showPasswordInput = false;
  exportPassword = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadPatientDetails(id);
    }
  }

  loadPatientDetails(id: string): void {
    const tenant = localStorage.getItem('tenant') || 'hospital_a';
    const headers = new HttpHeaders().set('X-Tenant-ID', tenant);

    this.http.get<any>(`http://localhost:8081/api/patients/${id}`, { headers }).subscribe({
      next: (data) => {
        this.patient = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load patient details. Check console for details.';
        this.loading = false;
        console.error('Patient details error:', err);
      }
    });
  }

  toggleExport(): void {
    this.showPasswordInput = !this.showPasswordInput;
  }

  exportPdf(): void {
    if (!this.exportPassword) {
      this.error = 'Please enter a password before downloading.';
      return;
    }

    this.loading = true;
    const url = `http://localhost:8081/api/export/patients/${this.patient.id}/history?password=${this.exportPassword}`;
    
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (blob) => {
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = `patient_history_${this.patient.id}.pdf`;
        link.click();
        this.showPasswordInput = false;
        this.exportPassword = '';
        this.loading = false;
      },
      error: (err) => {
        alert('Export failed. Check permissions.');
        this.loading = false;
      }
    });
  }

  // Helper for JSON data display
  getJsonString(obj: any): string {
    return JSON.stringify(obj, null, 2);
  }
}
