import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls:['./login.component.scss']
})
export class LoginComponent {
    loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.initializeForm();
  }

  initializeForm(): void {

    this.loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    tenant: ['', Validators.required]
  });
}


  login() {
    this.auth.login(this.loginForm.value).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => alert('Invalid login')
    });
  }
}
