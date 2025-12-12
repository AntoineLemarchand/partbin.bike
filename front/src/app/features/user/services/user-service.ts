import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserDto } from '../../../shared/models/UserDto';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  private apiUrl = 'http://localhost:8080/user'

  updateUser(data: Partial<{email: string | null, username: string | null}>): Observable<UserDto> {
    return this.http.post(this.apiUrl, data, {withCredentials: true}) as Observable<UserDto>
  }
}

