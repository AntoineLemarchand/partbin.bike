import { Component } from "@angular/core";
import { NgIcon, provideIcons } from "@ng-icons/core";
import { matPersonOutline } from "@ng-icons/material-icons/baseline";
import { matLoginOutline, matShoppingBasketOutline } from "@ng-icons/material-icons/outline";

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  imports: [ NgIcon ],
  viewProviders: [provideIcons({ matPersonOutline, matLoginOutline, matShoppingBasketOutline})]
})

export class NavbarComponent {
  name = "partbin.bike";
  isConnected = false;
}
