import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';

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

  exportToExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export`, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
    });
  }

  importFromExcel(formData: FormData): Observable<string> {
    return this.http.post<{message?: string, error?: string}>(`${this.apiUrl}/import`, formData).pipe(
      map(response => response.message || response.error || 'Operation completed'),
      catchError(error => {
        const errorMsg = error.error?.message || error.error?.error || error.message || 'Failed to import users';
        return throwError(() => errorMsg);
      })
    );
  }
}
