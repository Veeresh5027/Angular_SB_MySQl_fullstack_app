import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { User, UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  standalone: true, // Make it standalone
  imports: [CommonModule] // Import CommonModule
})
export class UserListComponent implements OnInit {
  users: User[] = [];

  constructor(
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.fetchUsers();
  }

  // Fetch users from the service
  fetchUsers(): void {
    this.userService.getUsers().subscribe({
      next: (users: User[]) => {
        this.users = users;
      },
      error: (error) => {
        console.error('Error fetching users', error);
        this.toastr.error('Failed to load users', 'Error', { timeOut: 3000 });
      }
    });
  }

  // Delete user based on ID
  deleteUser(id: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.toastr.success('User deleted successfully', 'Success', { timeOut: 3000 });
          this.fetchUsers();
        },
        error: (error) => {
          console.error('Error deleting user', error);
          this.toastr.error('Failed to delete user', 'Error', { timeOut: 3000 });
        }
      });
    }
  }

  // Edit user based on ID
  editUser(id: number): void {
    if (id === undefined || id === null) {
      this.toastr.error('Invalid user ID', 'Error', { timeOut: 3000 });
      return;
    }
    this.router.navigate([`/edit-user/${id}`]);
  }

  // Navigate to add user form
  addUser(): void {
    this.router.navigate(['/add-user']);
  }
}
