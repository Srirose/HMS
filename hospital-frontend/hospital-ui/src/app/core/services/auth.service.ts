import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private API = 'http://localhost:8080/auth/login';

  constructor(private http: HttpClient) {}

  login(data: { username: string; password: string; tenant: string }) {
    const headers = new HttpHeaders({
      'X-Tenant-ID': data.tenant
    });

    return this.http.post<{ token: string }>(this.API, {
      username: data.username,
      password: data.password
    }, { headers });
  }

  storeAuth(token: string, tenant: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('tenant', tenant);
  }

  clearAuth(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('tenant');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getRoles(): string[] {
    const token = localStorage.getItem('token');
    if (!token) {
      return [];
    }
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.roles || [];
    } catch {
      return [];
    }
  }
}
