import { Component, inject, Input, SimpleChange } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { UserDto } from "../../../../shared/models/UserDto";
import { UserService } from "../../services/user-service";

@Component({
  selector: 'app-profile-form',
  standalone: true,
  templateUrl: './profile-form.component.html',
  styleUrl: './profile-form.component.css',
  imports: [ReactiveFormsModule],
})
export class ProfileForm {
  @Input() user: UserDto | null = null;

  userService = inject(UserService)

  profileForm = new FormGroup({
    email: new FormControl(this.user?.email ?? '', [Validators.email]),
    username: new FormControl(this.user?.displayName ?? '')
  })

  ngOnChanges(changes: { user: UserDto } ): void {
    if (changes['user'] && this.user) {
      this.profileForm.patchValue({
        email: this.user.email,
        username: this.user.displayName
      });
    }
  }

  get email() {return this.profileForm.get("email") }
  get username() {return this.profileForm.get("username") }

  submitted = false;
  errorMessage: string | null = null
  successMessage: string | null = null

  onSubmit() {
    this.submitted = true;
    if (this.profileForm.valid) {
      this.errorMessage = null;
      this.successMessage = null;

      this.userService.updateUser(this.profileForm.value)
      .subscribe({
        next: () => {
            this.successMessage = "Profile Successfully updated"
        },
        error: () => {
          this.errorMessage = "Username or password invalid. Please try again";
          this.submitted = false;
        }
      })
    }
  }
}
