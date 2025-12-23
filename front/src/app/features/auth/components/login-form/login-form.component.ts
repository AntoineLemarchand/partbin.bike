import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth-service";
import { filter } from "rxjs";

@Component({
  selector: 'app-login-form',
  standalone: true,
  templateUrl: './login-form.component.html',
  styleUrl : './login-form.component.css',
  imports: [ReactiveFormsModule],
})

export class LoginForm {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  })
  get email() { return this.loginForm.get("email") }
  get password() { return this.loginForm.get("password") }
  get isSubmitted() {return this.submitted }

  submitted = false;
  errorMessage: string | null = null
  successMessage: string | null = null

  private authService = inject(AuthService)
  private router = inject(Router)


  onSubmit() {
    this.submitted = true;
    if (this.loginForm.valid) {
      this.errorMessage = null;
      this.successMessage = null;

      this.authService.submitLoginForm(this.loginForm.value)
      .subscribe({
        next: () => {
            this.authService.isAuthenticated.set(true)
            this.router.navigateByUrl('/user')
        },
        error: () => {
          this.errorMessage = "Username or password invalid. Please try again";
          this.submitted = false;
        }
      })
    }
  }
}
