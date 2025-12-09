import { Component } from '@angular/core';
import { SignupForm } from '../../components/signup-form/signup-form.component';

@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.page.html',
  styleUrl: './signup.page.css',
  imports: [SignupForm]
})
export class SignupPage { }

