import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ChatService, Chat } from '../../services/chat-service';

@Component({
  selector: 'app-chat-list-page',
  standalone: true,
  templateUrl: './chat-list.page.html',
  styleUrls: ['./chat-list.page.css'],
  imports: [CommonModule, RouterLink]
})
export class ChatListPageComponent implements OnInit {
  myProductChats = signal<Chat[]>([]);
  otherProductChats = signal<Chat[]>([]);
  loading = signal<boolean>(true);
  error = signal<boolean>(false);

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.loadChats();
  }

  private loadChats(): void {
    this.chatService.getUserChats().subscribe({
      next: (chats) => {
        const myProducts = chats.filter(chat => this.isMyProductChat(chat));
        const otherProducts = chats.filter(chat => !this.isMyProductChat(chat));

        this.myProductChats.set(myProducts);
        this.otherProductChats.set(otherProducts);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  private isMyProductChat(chat: Chat): boolean {
    return chat.product.owner.id === chat.user.id;
  }

  getLastMessage(chat: Chat): string {
    if (chat.messages.length === 0) {
      return 'No messages yet';
    }
    const lastMessage = chat.messages[chat.messages.length - 1];
    return lastMessage.content.length > 50
      ? lastMessage.content.substring(0, 50) + '...'
      : lastMessage.content;
  }

  formatTimestamp(timestamp: string): string {
    if (!timestamp) return '';

    const date = new Date(timestamp);
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const messageDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());

    if (messageDate.getTime() === today.getTime()) {
      return date.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
      });
    } else {
      return date.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric'
      });
    }
  }
}
