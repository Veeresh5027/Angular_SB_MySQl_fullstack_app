import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id?: number; // Optional ID for new users
  name: string;
  email: string;
  mobileNo: string;
  address: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/users'; // Backend API URL

  constructor(private http: HttpClient) {}

  // ✅ Fetch all users
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  // ✅ Get user by ID
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  // ✅ Add a new user
  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  // ✅ Update an existing user (Fix: Ensure it takes both ID and user object)
  updateUser(user: User): Observable<User> {
    if (!user.id) {
      throw new Error("User ID is required for update");
    }
    return this.http.put<User>(`${this.apiUrl}/${user.id}`, user);
  }

  // ✅ Delete a user
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
