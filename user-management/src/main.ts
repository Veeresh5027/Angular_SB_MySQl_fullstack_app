import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { importProvidersFrom } from '@angular/core';
import { provideToastr } from 'ngx-toastr';
import { provideHttpClient } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // ✅ Required for Toastr
import { AppComponent } from './app/app.component';
import { UserListComponent } from './app/components/user-list/user-list.component';
import { AddUserComponent } from './app/components/add-user/add-user.component';
import { EditUserComponent } from './app/components/edit-user/edit-user.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter([
      { path: '', component: UserListComponent },
      { path: 'add-user', component: AddUserComponent },
      { path: 'edit-user/:id', component: EditUserComponent },
    ]),
    importProvidersFrom(BrowserAnimationsModule), // ✅ Required for Toastr
    provideToastr(), // ✅ Toastr setup
    provideHttpClient(),
  ],
}).catch((err) => console.error(err));
