import { Routes } from '@angular/router';
import { ChatPageComponent } from './pages/chat/chat.page';

export const chatRoutes: Routes = [
  {
    path: 'product/:productId',
    component: ChatPageComponent,
    title: 'Product Chat'
  },
  {
    path: ':chatId',
    component: ChatPageComponent,
    title: 'Chat'
  }
];