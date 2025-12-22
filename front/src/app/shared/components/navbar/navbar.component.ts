import { Component, inject } from "@angular/core";
import { NgIcon, provideIcons } from "@ng-icons/core";
import { matPersonOutline } from "@ng-icons/material-icons/baseline";
import { matChatOutline, matLoginOutline, matLogoutOutline, matShoppingBasketOutline } from "@ng-icons/material-icons/outline";
import { AuthService } from "../../../features/auth/services/auth-service";

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  imports: [ NgIcon ],
  viewProviders: [provideIcons({ matPersonOutline, matLoginOutline, matLogoutOutline, matShoppingBasketOutline, matChatOutline})]
})

export class NavbarComponent {
  name = "partbin.bike";
  authService = inject(AuthService)
  isConnected = this.authService.isAuthenticated;

  logout() {
    this.authService.logout().subscribe({
      next: () => this.authService.isAuthenticated.set(false)
    })
  }
}
