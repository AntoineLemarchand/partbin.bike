import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalButton } from '../../../shared/components/modal/modal-button.component';
import { matPlusOutline } from '@ng-icons/material-icons/outline';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { Modal } from '../../../shared/components/modal/modal.component';
import { ProductForm } from '../../product/components/product-form/product-form.component';
import { ProductService, Product } from '../../product/services/product-service';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: "./home.page.html",
  styleUrl: "./home.page.css",
  imports: [CommonModule, ModalButton, NgIcon, Modal, ProductForm],
  viewProviders: [provideIcons({matPlusOutline})]
})
export class HomePage implements OnInit {
  products = signal<Product[]>([]);
  loading = signal(true);

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.loading.set(true)
    console.log('Loading products...');
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products.set(products)
        this.loading.set(false)
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.loading.set(false)
      }
    });
  }
}

