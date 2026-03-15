import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      hospital: ['hospital_a', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { username, password, hospital } = this.loginForm.value;
      this.authService.login({ username, password }, hospital).subscribe({
        next: (res) => {
          const role = this.authService.getUserRole();
          if (role === 'ADMIN') {
            this.router.navigate(['/admin']);
          } else if (role === 'DOCTOR') {
            this.router.navigate(['/doctor']);
          } else if (role === 'NURSE') {
            this.router.navigate(['/nurse']);
          } else {
            this.errorMessage = 'Unknown role';
          }
        },
        error: (err) => {
          this.errorMessage = err.error || 'Invalid username or password';
          console.error(err);
        }
      });
    }
  }
}
