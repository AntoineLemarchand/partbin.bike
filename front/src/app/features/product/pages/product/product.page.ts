import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService, Product } from '../../services/product-service';

@Component({
  selector: 'app-product-page',
  standalone: true,
  templateUrl: './product.page.html',
  styleUrls: ['./product.page.css'],
  imports: [CommonModule]
})
export class ProductPageComponent implements OnInit {
  product = signal<Product | null>(null);
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.loadProduct(Number(productId));
    } else {
      this.error = true;
      this.loading = false;
    }
  }

  private loadProduct(productId: number): void {
    this.productService.getProductById(productId).subscribe({
      next: (loadedProduct) => {
        this.product.set(loadedProduct)
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  contactSeller(): void {
    if (this.product()) {
      this.router.navigate(['/chat/product', this.product()!.id]);
    }
  }

  goBack(): void {
    window.history.back();
  }
}
