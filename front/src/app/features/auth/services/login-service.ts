import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private apiUrl = 'http://localhost:8080/auth/login'

    constructor(private http: HttpClient) {}

  submitForm(data: any): Observable<Object> {
    return this.http.post(this.apiUrl, data);
  }
}
