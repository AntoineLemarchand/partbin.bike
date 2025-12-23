import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompatClient, Stomp, StompSubscription } from '@stomp/stompjs';

export interface Message {
  id: number;
  content: string;
  sender: {
    id: number;
    name: string;
  };
  sentOn: string;
}

export interface Chat {
  id: number;
  product: {
    id: number;
    name: string;
    owner: {
      id: number,
      name: string
    }
  };
  user: {
    id: number;
    displayName: string;
  };
  messages: Message[];
}

@Injectable({
  providedIn: 'root'
})
export class ChatService implements OnDestroy {
  private apiUrl = 'http://localhost:8080/chats';
  private websocketUrl = 'ws://localhost:8080/ws';

  private connection: CompatClient | undefined;
  private subscription: StompSubscription | undefined;

  constructor(private http: HttpClient) {
    this.connection = Stomp.client(this.websocketUrl);
    this.connection.connect({}, () => {});
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    if (this.connection && this.connection.connected) {
      this.connection.disconnect();
    }
  }

  getOrCreateChat(productId: number): Observable<Chat> {
    return this.http.post<Chat>(`${this.apiUrl}/join/${productId}`, {}, { withCredentials: true });
  }

  getChatById(chatId: number): Observable<Chat> {
    return this.http.get<Chat>(`${this.apiUrl}/${chatId}`, { withCredentials: true });
  }

  getUserChats(): Observable<Chat[]> {
    return this.http.get<Chat[]>(this.apiUrl, { withCredentials: true });
  }

  sendMessage(chatId: number, message: string): void {
    if (this.connection && this.connection.connected) {
      this.connection.send(
        `/ws/message/${chatId}`,
        { 'content-type': 'application/json' },
        JSON.stringify({content: message})
      );
    }
  }

  public listen(chatId: number, fun: Function): void {
    if (this.connection && this.connection.connected) {
      this.subscription = this.connection.subscribe(`/chats/${chatId}`, message => fun(JSON.parse(message.body)));
    } else {
      const checkConnection = () => {
        if (this.connection && this.connection.connected) {
          this.subscription = this.connection.subscribe(`/chats/${chatId}`, message => fun(JSON.parse(message.body)));
        } else {
          setTimeout(checkConnection, 100);
        }
      };
      checkConnection();
    }
  }

  sortMessagesByDate(chat: Chat): Chat {
    chat.messages.sort((a, b) => new Date(a.sentOn).getTime() - new Date(b.sentOn).getTime());
    return chat;
  }
}

