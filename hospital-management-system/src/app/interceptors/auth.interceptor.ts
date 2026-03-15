import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const tenant = localStorage.getItem('tenant') || 'hospital_a';

  let headers: { [key: string]: string } = {
    'X-Tenant-ID': tenant
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const authReq = req.clone({
    setHeaders: headers
  });

  console.log(`Interceptor: Attaching tenant ${tenant} to request: ${req.url}`);
  return next(authReq);
};
