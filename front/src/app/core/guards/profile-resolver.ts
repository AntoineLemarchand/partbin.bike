import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth-service';
import { catchError, of } from 'rxjs';
import { UserDto } from '../../shared/models/UserDto';

export const profileResolver: ResolveFn<UserDto | null> = () => {
  const authService = inject(AuthService);

  return authService.getCurrentUser().pipe(
    catchError(() => of(null))
  );
};

