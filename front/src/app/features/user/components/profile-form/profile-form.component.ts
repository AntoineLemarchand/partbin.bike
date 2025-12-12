import { Component, Input, SimpleChange } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { UserDto } from "../../../../shared/models/UserDto";

@Component({
  selector: 'app-profile-form',
  standalone: true,
  templateUrl: './profile-form.component.html',
  styleUrl: './profile-form.component.css',
  imports: [ReactiveFormsModule],
})
export class ProfileForm {
  @Input() user: UserDto | null = null;


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
}
