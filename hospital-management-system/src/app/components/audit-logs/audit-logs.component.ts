import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-audit-logs',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './audit-logs.component.html',
  styleUrls: ['./audit-logs.component.scss']
})
export class AuditLogsComponent implements OnInit {
  logs: any[] = [];
  filteredLogs: any[] = [];
  searchTerm: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadLogs();
  }

  loadLogs(): void {
    const tenant = localStorage.getItem('tenant') || 'hospital_a';
    const headers = new HttpHeaders().set('X-Tenant-ID', tenant);
    this.http.get<any[]>('http://localhost:8081/api/audit/logs', { headers }).subscribe({
      next: (data) => {
        this.logs = data;
        this.applyFilter();
      },
      error: (err) => console.error('Failed to load logs', err)
    });
  }

  applyFilter(): void {
    if (!this.searchTerm) {
      this.filteredLogs = [...this.logs];
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredLogs = this.logs.filter(log => 
        log.userId?.toLowerCase().includes(term) || 
        log.entityName?.toLowerCase().includes(term) ||
        log.action?.toLowerCase().includes(term)
      );
    }
  }
}
