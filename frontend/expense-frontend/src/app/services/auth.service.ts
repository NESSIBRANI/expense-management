import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, switchMap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthStateService } from './auth-state.service';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`;
  private userLoaded = false;

 constructor(
  private http: HttpClient,
  private authState: AuthStateService
) {}

  // =====================
  // AUTH
  // =====================

  register(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  // ⭐ LOGIN COMPLET (login + récupération user)
  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password }).pipe(

      // 1️⃣ Stocker le token
      tap(res => {
        localStorage.setItem('token', res.token);
      }),

      // 2️⃣ Charger l'utilisateur AVANT navigation
      switchMap(() => this.loadUser())
    );
  }

  // =====================
  // USER INFO
  // =====================

  me(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me`);
  }

 loadUser(): Observable<any> {
  return this.me().pipe(
    tap((user) => {

      const cleanRole = user.role.replace('ROLE_', '');

      localStorage.setItem('userId', String(user.id));
      localStorage.setItem('role', cleanRole);
      localStorage.setItem('name', user.name || '');
      localStorage.setItem('email', user.email || '');

      this.userLoaded = true;

      // ⭐⭐⭐ LA LIGNE QUI RÉPARE TOUT
      this.authState.setUserReady();

    })
  );
}



  // =====================
  // HELPERS
  // =====================

  isUserLoaded(): boolean {
    return this.userLoaded;
  }

  getUserId(): number {
    return Number(localStorage.getItem('userId'));
  }

  getRole(): string {
  return (localStorage.getItem('role') || '').toUpperCase();
}



  getName(): string {
    return localStorage.getItem('name') || '';
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  logout(): void {
  localStorage.clear();
  this.userLoaded = false;
  this.authState.reset();
}

}
