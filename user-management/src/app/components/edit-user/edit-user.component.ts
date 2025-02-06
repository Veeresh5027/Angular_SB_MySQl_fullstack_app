import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms'
import { ToastrService } from 'ngx-toastr';
import { User, UserService } from '../../services/user.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css'],
  imports: [FormsModule]
})
export class EditUserComponent implements OnInit {
  user: User = {
    id: 0,
    name: '',
    email: '',
    mobileNo: '',
    address: ''
  };

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id')); // Get ID from route params
    if (isNaN(id) || id <= 0) {
      this.toastr.error('Invalid user ID', 'Error', { timeOut: 3000 });
      return;
    }
  
    this.userService.getUserById(id).subscribe({
      next: (user: User) => {
        this.user = user;
      },
      error: (error) => {
        console.error('Error fetching user', error);
        this.toastr.error('Failed to fetch user details', 'Error', { timeOut: 3000 });
      }
    });
  }
  

  updateUser(): void {
    if (!this.user.name || !this.user.email || !this.user.mobileNo || !this.user.address) {
      this.toastr.warning('All fields are required!', 'Validation Error', { timeOut: 3000 });
      return;
    }

    this.userService.updateUser(this.user).subscribe({
      next: () => {
        this.toastr.success('User updated successfully!', 'Success', { timeOut: 3000 });
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Error updating user', error);
        this.toastr.error('Failed to update user', 'Error', { timeOut: 3000 });
      }
    });
  }
}
