import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard-nurse',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './dashboard-nurse.component.html',
  styleUrl: './dashboard-nurse.component.scss'
})
export class DashboardNurseComponent {
  constructor(private authService: AuthService, private router: Router) {}

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
