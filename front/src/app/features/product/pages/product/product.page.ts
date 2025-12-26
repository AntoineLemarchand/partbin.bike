import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService, Product, UpdateProductRequest } from '../../services/product-service';
import { AuthService } from '../../../auth/services/auth-service';
import { UserDto } from '../../../../shared/models/UserDto';

@Component({
  selector: 'app-product-page',
  standalone: true,
  templateUrl: './product.page.html',
  styleUrls: ['./product.page.css'],
  imports: [CommonModule, FormsModule]
})
export class ProductPageComponent implements OnInit {
  product = signal<Product | null>(null);
  loading = true;
  error = false;
  selectedFiles: File[] = [];
  uploadProgress = false;
  uploadMessage: string | null = null;
  currentUser = signal<UserDto | null>(null);
  isOwner = signal<boolean>(false);
  isEditing = signal<boolean>(false);
  editedName = signal<string>('');
  editedDescription = signal<string>('');
  updateMessage: string | null = null;
  updateProgress = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.loadCurrentUser();
      this.loadProduct(Number(productId));
    } else {
      this.error = true;
      this.loading = false;
    }
  }

  private loadCurrentUser(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.currentUser.set(user);
        this.checkOwnership();
      },
      error: () => {
        this.currentUser.set(null);
        this.checkOwnership();
      }
    });
  }

  private loadProduct(productId: number): void {
    this.productService.getProductById(productId).subscribe({
      next: (loadedProduct) => {
        this.product.set(loadedProduct);
        this.checkOwnership();
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  private checkOwnership(): void {
    const currentProduct = this.product();
    const currentUser = this.currentUser();
    
    if (currentProduct && currentUser) {
      this.isOwner.set(currentProduct.owner.id === currentUser.id);
    } else {
      this.isOwner.set(false);
    }
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

  startEditing(): void {
    const currentProduct = this.product();
    if (currentProduct) {
      this.editedName.set(currentProduct.name);
      this.editedDescription.set(currentProduct.description);
      this.isEditing.set(true);
      this.updateMessage = null;
    }
  }

  cancelEditing(): void {
    this.isEditing.set(false);
    this.editedName.set('');
    this.editedDescription.set('');
    this.updateMessage = null;
  }

  saveChanges(): void {
    const currentProduct = this.product();
    if (!currentProduct) return;

    const updateData: UpdateProductRequest = {
      name: this.editedName(),
      description: this.editedDescription()
    };

    this.updateProgress = true;
    this.updateMessage = null;

    this.productService.updateProduct(currentProduct.id, updateData).subscribe({
      next: (updatedProduct) => {
        this.product.set(updatedProduct);
        this.isEditing.set(false);
        this.updateMessage = 'Product updated successfully!';
        this.updateProgress = false;
        
        setTimeout(() => {
          this.updateMessage = null;
        }, 3000);
      },
      error: () => {
        this.updateMessage = 'Failed to update product. Please try again.';
        this.updateProgress = false;
      }
    });
  }
}
