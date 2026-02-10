import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class TokenTenantInterceptor implements HttpInterceptor {

  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('token');
    const tenant = localStorage.getItem('tenant');

    let headers = req.headers;
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    if (tenant) {
      headers = headers.set('X-Tenant-ID', tenant);
    }

    const cloned = req.clone({ headers });
    return next.handle(cloned);
  }
}


