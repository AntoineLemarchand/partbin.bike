import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { LoginService } from "../../services/login-service";
import { Router } from "@angular/router";

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
    password: new FormControl('', [Validators.required, Validators.minLength(8)]),
  })
  get email() { return this.loginForm.get("email") }
  get password() { return this.loginForm.get("password") }
  get isSubmitted() {return this.submitted }

  submitted = false;
  errorMessage: string | null = null
  successMessage: string | null = null

  private loginService = inject(LoginService)
  private router = inject(Router)


  onSubmit() {
    this.submitted = true;
    if (this.loginForm.valid) {
      this.errorMessage = null;
      this.successMessage = null;

      this.loginService.submitForm(this.loginForm.value)
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
