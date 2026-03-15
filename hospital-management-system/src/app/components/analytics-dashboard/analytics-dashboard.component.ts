import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartType, ChartData } from 'chart.js';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-analytics-dashboard',
  standalone: true,
  imports: [CommonModule, BaseChartDirective, RouterModule],
  templateUrl: './analytics-dashboard.component.html',
  styleUrls: ['./analytics-dashboard.component.scss']
})
export class AnalyticsDashboardComponent implements OnInit {
  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { display: true },
      title: { display: true, text: 'MedNex Hospital Analytics' }
    }
  };

  public barChartLabels: string[] = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;

  public barChartData: ChartData<'bar'> = {
    labels: this.barChartLabels,
    datasets: [
      { data: [0, 0, 0, 0, 0, 0, 0], label: 'Patient Admissions' },
      { data: [0, 0, 0, 0, 0, 0, 0], label: 'Bed Occupancy' }
    ]
  };

  public pieChartData: ChartData<'pie'> = {
    labels: ['Available', 'Occupied'],
    datasets: [{ data: [200, 0], backgroundColor: ['#28a745', '#dc3545'] }]
  };

  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
      title: { display: true, text: 'Total Bed Occupancy' }
    }
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    const apiUrl = 'http://localhost:8081/api/metrics';
    
    // Load Bed Occupancy
    this.http.get<any>(`${apiUrl}/bed-occupancy`).subscribe(data => {
      this.pieChartData = {
        labels: ['Available', 'Occupied'],
        datasets: [{
          data: [data.totalBeds - data.occupiedBeds, data.occupiedBeds],
          backgroundColor: ['#28a745', '#dc3545']
        }]
      };
    });

    // Load Admission Trends
    this.http.get<any>(`${apiUrl}/admission-trends`).subscribe(data => {
      const values = this.barChartLabels.map(label => data[label] || 0);
      this.barChartData = {
        ...this.barChartData,
        datasets: [
          { ...this.barChartData.datasets[0], data: values },
          this.barChartData.datasets[1]
        ]
      };
    });
  }
}
