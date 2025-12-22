import { Routes } from '@angular/router';
import { ChatPageComponent } from './pages/chat/chat.page';
import { ChatListPageComponent } from './pages/chat-list/chat-list.page';

export const chatRoutes: Routes = [
  {
    path: '',
    component: ChatListPageComponent,
    title: 'Chats'
  },
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