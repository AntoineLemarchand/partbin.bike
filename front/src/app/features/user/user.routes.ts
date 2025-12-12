import { Routes } from '@angular/router';
import { ProfilePage } from './profile/profile.page';
import { profileResolver } from '../../core/guards/profile-resolver';

export const HOME_ROUTES: Routes = [
  {
    path: '',
    component: ProfilePage,
    resolve: {
      user: profileResolver
    }
  }
];

