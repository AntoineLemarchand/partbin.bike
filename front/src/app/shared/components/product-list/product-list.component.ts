import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Product } from '../../../features/product/services/product-service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
  imports: [CommonModule]
})
export class ProductListComponent {
  @Input() products: Product[] = [];
  @Input() loading = false;
  @Input() title = 'Products';
  @Input() emptyMessage = 'No products available';
  @Input() emptyDescription = 'Be the first to add a product to the marketplace!';
  @Input() loadingMessage = 'Loading products...';

  constructor(private router: Router) {}

  viewProduct(productId: number): void {
    this.router.navigate(['/product', productId]);
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath) return '';
    if (imagePath.startsWith('http')) return imagePath;
    return `http://localhost:8080/products/${imagePath}`;
  }
}
