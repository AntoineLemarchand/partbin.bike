import { Component, OnInit, ViewChild, ElementRef, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ChatService, Chat, Message } from '../../services/chat-service';
import { toSignal } from '@angular/core/rxjs-interop';
import { UserDto } from '../../../../shared/models/UserDto';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-chat-page',
  standalone: true,
  templateUrl: './chat.page.html',
  styleUrls: ['./chat.page.css'],
  imports: [CommonModule, FormsModule]
})
export class ChatPageComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private data = toSignal(this.route.data);
  user = computed(() => this.data()?.['profile'] as UserDto | null);

  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  chatService = inject(ChatService)

  chat = signal<Chat | null>(null);
  loading = signal<boolean>(true);
  error = signal<boolean>(false);
  newMessage = signal<string>('');
  correspondantName = signal<string>("...")

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('productId');
    const chatId = this.route.snapshot.paramMap.get('chatId');

    if (productId) {
      this.loadChat(this.chatService.getOrCreateChat(Number(productId)))
    } else if (chatId) {
      this.loadChat(this.chatService.getChatById(Number(chatId)))
    } else {
      this.error.set(true);
      this.loading.set(false);
    }
  }

  private loadChat(list: Observable<Chat>) {
    list.subscribe({
      next: (loadedChat) => {
        this.correspondantName.set(loadedChat.user.displayName == this.user()?.displayName ? loadedChat.product.owner.name : loadedChat.user.displayName)
        this.chat.set(this.chatService.sortMessagesByDate(loadedChat))
        this.loading.set(false)
        setTimeout(() => this.scrollToBottom(), 100);
        this.chatService.listen(this.chat()!.id, (message: Message) => this.addMessage(message))
      },
      error: () => {
        this.error.set(true)
        this.loading.set(false)
      }
    })
  }

  addMessage(message: Message): void {
    const currentChat = this.chat();
    if (currentChat && currentChat.messages) {
      const updatedMessages = [...currentChat.messages, message];
      this.chat.set({
        ...currentChat,
        messages: updatedMessages
      });
      setTimeout(() => this.scrollToBottom(), 100);
    }
  }

  sendMessage(): void {
    if (!this.newMessage().trim() || !this.chat()) {
      return;
    }
    this.chatService.sendMessage(this.chat()!.id, this.newMessage().trim())
    this.newMessage.set("")
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

    const optionsTime: Intl.DateTimeFormatOptions = {
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    };
    const optionsDate: Intl.DateTimeFormatOptions = {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    };

    if (messageDate.getTime() === today.getTime()) {
      return date.toLocaleTimeString('en-US', optionsTime);
    } else {
      return date.toLocaleString('en-US', optionsDate);
    }
  }


  isOwnMessage(message: Message): boolean {
    return message.sender.id === this.user()?.id
  }
}
