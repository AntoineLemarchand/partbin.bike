import { Component } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrl : './login-form.component.css',
  imports: [ReactiveFormsModule]
})

export class LoginForm {
  loginForm = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
  })

  onSubmit() {
    console.warn(this.loginForm.value);
  }
}
