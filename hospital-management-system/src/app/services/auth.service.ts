import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/auth';
  private currentTenant: string = 'hospital_a'; // Default

  constructor(private http: HttpClient) {
    const storedTenant = localStorage.getItem('tenant');
    if (storedTenant) {
      this.currentTenant = storedTenant;
    }
  }

  setTenant(tenant: string) {
    this.currentTenant = tenant;
    localStorage.setItem('tenant', tenant);
  }

  getTenant(): string {
    return this.currentTenant;
  }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Tenant-ID': this.currentTenant
    });
  }

  register(user: any, tenant: string): Observable<any> {
    this.setTenant(tenant);
    return this.http.post(`${this.apiUrl}/register`, user, { 
      responseType: 'text' 
    });
  }

  login(credentials: any, tenant: string): Observable<any> {
    this.setTenant(tenant);
    return this.http.post(`${this.apiUrl}/login`, credentials, { observe: 'response' }).pipe(
      tap((resp: any) => {
        const body = resp?.body || {};
        let token = body?.token || body?.accessToken || body?.jwt || body?.id_token;
        if (!token) {
          const authHeader = resp?.headers?.get?.('Authorization');
          if (authHeader) {
            const match = authHeader.match(/^Bearer\s+(.+)$/i);
            if (match) token = match[1];
          }
        }
        if (token) {
          localStorage.setItem('token', token);
          const role = body?.role || (body?.authorities?.[0] ?? null);
          if (role) localStorage.setItem('role', role);
          localStorage.setItem('username', credentials.username);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getUserRole(): string | null {
    return localStorage.getItem('role');
  }
}
