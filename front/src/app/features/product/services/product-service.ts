import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CreateProductRequest {
  name: string;
  description: string;
  categoryId: number;
}

export interface Category {
  id: number;
  name: string;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  category: {
    id: number,
    name: string
  },
  owner: {
    id: number,
    name: string
  }
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/products'

  constructor(private http: HttpClient) {}

  createProduct(product: CreateProductRequest): Observable<string> {
    return this.http.post<string>(this.apiUrl, product, {withCredentials: true});
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl, {withCredentials: true});
  }

  getMyProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/my`, {withCredentials: true});
  }

  getMyWishlist(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/wishlist`, {withCredentials: true});
  }

  addToWishlist(productId: number): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/wishlist/${productId}`, {}, {withCredentials: true});
  }

  removeFromWishlist(productId: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/wishlist/${productId}`, {withCredentials: true});
  }

  getProductById(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${productId}`, {withCredentials: true});
  }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>('http://localhost:8080/categories', {withCredentials: true});
  }
}
