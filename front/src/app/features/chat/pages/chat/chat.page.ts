import { Component, OnInit, ViewChild, ElementRef, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ChatService, Chat, Message } from '../../services/chat-service';

@Component({
  selector: 'app-chat-page',
  standalone: true,
  templateUrl: './chat.page.html',
  styleUrls: ['./chat.page.css'],
  imports: [CommonModule, FormsModule]
})
export class ChatPageComponent implements OnInit {
  chat = signal<Chat | null>(null);
  loading = signal<boolean>(true);
  error = signal<boolean>(false);
  newMessage = signal<string>('');
  sending = signal<boolean>(false);

  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  constructor(
    private route: ActivatedRoute,
    private chatService: ChatService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('productId');
    const chatId = this.route.snapshot.paramMap.get('chatId');

    if (productId) {
      this.loadChatByProductId(Number(productId));
    } else if (chatId) {
      this.loadChatById(Number(chatId));
    } else {
      this.error.set(true);
      this.loading.set(false);
    }
  }

  private loadChatByProductId(productId: number): void {
    this.chatService.getOrCreateChat(productId).subscribe({
      next: (loadedChat) => {
        this.chat.set(loadedChat)
        this.loading.set(false)
        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: () => {
        this.error.set(true)
        this.loading.set(false)
      }
    });
  }

  private loadChatById(chatId: number): void {
    this.chatService.getChatById(chatId).subscribe({
      next: (loadedChat) => {
        this.chat.set(loadedChat)
        this.loading.set(false)
        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: () => {
        this.error.set(true)
        this.loading.set(false)
      }
    });
  }

  sendMessage(): void {
    if (!this.newMessage().trim() || !this.chat() || this.sending()) {
      return;
    }

    this.sending.set(true);
    this.chatService.sendMessage(this.chat()!.id, this.newMessage().trim()).subscribe({
      next: (message) => {
        const currentChat = this.chat();
        if (currentChat) {
          currentChat.messages.push(message);
          this.chat.set(currentChat);
          this.newMessage.set('');
          setTimeout(() => this.scrollToBottom(), 100);
        }
        this.sending.set(false);
      },
      error: () => {
        this.sending.set(false);
      }
    });
  }

  private scrollToBottom(): void {
    if (this.messagesContainer) {
      this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
    }
  }

  goBack(): void {
    window.history.back();
  }

  formatTimestamp(timestamp: string): string {
    const date = new Date(timestamp);
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const messageDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());

    if (messageDate.getTime() === today.getTime()) {
      // Show only time for today's messages
      return date.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
      });
    } else {
      // Show date and time for older messages
      return date.toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
      });
    }
  }

  isOwnMessage(message: Message): boolean {
    // This would need to be implemented based on current user ID
    // For now, we'll assume we can determine this from the message
    return message.sender.id === this.chat()?.user.id;
  }
}
