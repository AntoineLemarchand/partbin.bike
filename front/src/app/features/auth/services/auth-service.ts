import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { UserDto } from '../../../shared/models/UserDto';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {
    this.checkAuth();
  }

  private apiUrl = 'http://localhost:8080/auth'
    isAuthenticated = signal<boolean>(false)

  submitLoginForm(data: any): Observable<Object> {
    return this.http.post(this.apiUrl + "/login", data, {withCredentials: true});
  }

  getCurrentUser(): Observable<UserDto> {
    return this.http.get(this.apiUrl + '/me', {withCredentials: true}) as Observable<UserDto>;
  }

  checkAuth() {
    this.getCurrentUser().subscribe({
      next: () => this.isAuthenticated.set(true),
      error: () => this.isAuthenticated.set(false)
    })
  }

  submitSignupForm(data: any): Observable<Object> {
    return this.http.post(this.apiUrl + "/signup", data, {withCredentials: true});
  }

  logout() {
    return this.http.post(this.apiUrl + '/logout', {}, {withCredentials: true});
  }
}
