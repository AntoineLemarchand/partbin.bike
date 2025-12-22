import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Message {
  id: number;
  content: string;
  sender: {
    id: number;
    name: string;
  };
  timestamp: string;
}

export interface Chat {
  id: number;
  product: {
    id: number;
    name: string;
  };
  user: {
    id: number;
    name: string;
  };
  messages: Message[];
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/chats';

  constructor(private http: HttpClient) {}

  getOrCreateChat(productId: number): Observable<Chat> {
    return this.http.post<Chat>(`${this.apiUrl}/join/${productId}`, {}, {withCredentials: true});
  }

  getChatById(chatId: number): Observable<Chat> {
    return this.http.get<Chat>(`${this.apiUrl}/${chatId}`, {withCredentials: true});
  }

  getUserChats(): Observable<Chat[]> {
    return this.http.get<Chat[]>(this.apiUrl, {withCredentials: true});
  }

  sendMessage(chatId: number, message: string): Observable<Message> {
    return this.http.post<Message>(`${this.apiUrl}/message/${chatId}`, message, {withCredentials: true});
  }
}
