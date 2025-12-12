import { Routes } from '@angular/router';
import { profileResolver } from '../../core/guards/profile-resolver';
import { ProfilePage } from './pages/profile/profile.page';

export const HOME_ROUTES: Routes = [
  {
    path: '',
    component: ProfilePage,
    resolve: {
      user: profileResolver
    }
  }
];

