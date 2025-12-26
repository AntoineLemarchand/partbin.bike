import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CreateProductRequest {
  name: string;
  description: string;
  categoryId: number;
}

export interface UpdateProductRequest {
  name: string;
  description: string;
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
  },
  imagesUrl: string[]
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

  uploadImages(productId: number, files: File[]): Observable<string[]> {
    const formData = new FormData();
    files.forEach(file => formData.append('files', file));
    return this.http.post<string[]>(`${this.apiUrl}/${productId}/images`, formData, {withCredentials: true});
  }

  deleteImage(productId: number, imagePath: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${productId}/images?imagePath=${encodeURIComponent(imagePath)}`, {withCredentials: true});
  }

  updateProduct(productId: number, product: UpdateProductRequest): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${productId}`, product, {withCredentials: true});
  }
}
