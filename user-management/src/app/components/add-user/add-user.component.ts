import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'
import { ToastrService } from 'ngx-toastr';
import { User, UserService } from '../../services/user.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css'],
  imports: [FormsModule]
})
export class AddUserComponent {
  user: User = {
    id: 0,
    name: '',
    email: '',
    mobileNo: '',
    address: ''
  };

  constructor(private userService: UserService, private router: Router, private toastr: ToastrService) {}

  addUser(): void {
    if (!this.user.name || !this.user.email || !this.user.mobileNo || !this.user.address) {
      this.toastr.warning('All fields are required!', 'Validation Error', { timeOut: 3000 });
      return;
    }

    this.userService.addUser(this.user).subscribe({
      next: () => {
        this.toastr.success('User added successfully!', 'Success', { timeOut: 3000 });
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Error adding user', error);
        this.toastr.error('Failed to add user', 'Error', { timeOut: 3000 });
      }
    });
  }
}
