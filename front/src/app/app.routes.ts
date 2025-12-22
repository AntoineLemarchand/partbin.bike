import { Routes } from '@angular/router';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';
import { authGuard } from './core/guards/auth-guard';
import { profileResolver } from './core/guards/profile-resolver';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./features/home/home.routes').then(m => m.HOME_ROUTES)
      },
      {
        path: 'auth',
        loadChildren: () =>
          import('./features/auth/auth.routes').then(m => m.HOME_ROUTES)
      },
      {
        path: 'user',
        loadChildren: () =>
          import('./features/user/user.routes').then(m => m.HOME_ROUTES),
        canActivate: [authGuard],
      },
      {
        path: 'product',
        loadChildren: () =>
          import('./features/product/product.routes').then(m => m.productRoutes),
      },
    ]
  }
];

