import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
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
}
