import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CreateProductRequest {
  name: string;
  description: string;
  categoryId: number;
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
}
