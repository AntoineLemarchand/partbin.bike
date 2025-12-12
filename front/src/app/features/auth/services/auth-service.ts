import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {
    this.getCurrentUser().subscribe({
      next: user => this.isAuthenticated.set(!!user),
      error: () => this.isAuthenticated.set(false),
    })
  }

  private apiUrl = 'http://localhost:8080/auth'
  isAuthenticated = signal<boolean>(false)

  submitLoginForm(data: any): Observable<Object> {
    return this.http.post(this.apiUrl + "/login", data, {withCredentials: true});
  }

  getCurrentUser() {
    return this.http.get(this.apiUrl + '/me', {withCredentials: true});
  }

  submitSignupForm(data: any): Observable<Object> {
    return this.http.post(this.apiUrl + "/signup", data, {withCredentials: true});
  }

  logout() {
    return this.http.post(this.apiUrl + '/logout', {}, {withCredentials: true});
  }
}
