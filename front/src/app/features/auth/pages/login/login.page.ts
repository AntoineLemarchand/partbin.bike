import { Component } from '@angular/core';
import { LoginForm } from '../../components/login-form/login-form.component';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.page.html',
  styleUrl: './login.page.css',
  imports: [LoginForm]
})
export class LoginPage { }
