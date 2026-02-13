import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8081/api/users';

  constructor(private http: HttpClient) {}

  // ✅ backend: GET /api/users
  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl);
  }

  // ✅ backend: PUT /api/users/{id}
  update(user: User): Observable<User> {
    return this.http.put<User>(
      `${this.baseUrl}/${user.id}`,
      {
        name: user.name,
        email: user.email,
        role: user.role,
        enabled: user.enabled
      }
    );
  }

}
