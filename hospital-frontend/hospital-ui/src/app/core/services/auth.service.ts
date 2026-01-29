import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private API = 'http://localhost:8080/auth/login';

  constructor(private http: HttpClient) {}

  login(data: any) {
    const headers = new HttpHeaders({
      'X-Tenant-ID': data.tenant
    });

    return this.http.post<any>(this.API, {
      username: data.username,
      password: data.password
    }, { headers });
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
