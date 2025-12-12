import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth-service";

@Component({
  selector: 'app-signup-form',
  templateUrl: './signup-form.component.html',
  styleUrl : './signup-form.component.css',
  imports: [ReactiveFormsModule]
})

export class SignupForm {
  signupForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)]),
    confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
  })
  get username() { return this.signupForm.get("username") }
  get email() { return this.signupForm.get("email") }
  get password() { return this.signupForm.get("password") }
  get isSubmitted() {return this.submitted }

  submitted = false;
  errorMessage: string | null = null
  successMessage: string | null = null

  private authService = inject(AuthService)
  private router = inject(Router)

  onSubmit() {
    console.log(this.signupForm)
    this.submitted = true;
    if (this.signupForm.valid) {
      this.errorMessage = null;
      this.successMessage = null;

      this.authService.submitSignupForm(this.signupForm.value)
        .subscribe({
          next: () => {
            this.router.navigate(['/profile'])
          },
          error: () => {
            this.errorMessage = "Username or password invalid. Please try again";
            this.submitted = false;
          }
        })
    }
  }
}

