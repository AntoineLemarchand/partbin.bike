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
  selectedFiles: File[] = [];
  uploadProgress = false;
  uploadMessage: string | null = null;

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

  onFileSelect(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      this.selectedFiles = Array.from(fileList);
    }
  }

  uploadImages(): void {
    if (!this.selectedFiles.length || !this.product()) return;

    this.uploadProgress = true;
    this.uploadMessage = null;

    this.productService.uploadImages(this.product()!.id, this.selectedFiles).subscribe({
      next: () => {
        this.uploadMessage = 'Images uploaded successfully!';
        this.selectedFiles = [];
        this.loadProduct(this.product()!.id);
        this.uploadProgress = false;
      },
      error: () => {
        this.uploadMessage = 'Failed to upload images. Please try again.';
        this.uploadProgress = false;
      }
    });
  }

  deleteImage(imagePath: string): void {
    if (!this.product()) return;

    this.productService.deleteImage(this.product()!.id, imagePath).subscribe({
      next: () => {
        this.loadProduct(this.product()!.id);
      },
      error: () => {
        console.error('Failed to delete image');
      }
    });
  }

  goBack(): void {
    window.history.back();
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath) return '';
    // If the path already starts with http, return as is
    if (imagePath.startsWith('http')) return imagePath;
    // Prepend the backend API base URL
    return `http://localhost:8080/products/${imagePath}`;
  }
}
