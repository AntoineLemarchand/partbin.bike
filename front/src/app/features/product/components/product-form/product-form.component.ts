import { Component, inject, OnInit } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ProductService, CreateProductRequest, Category } from "../../services/product-service";
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-product-form',
  standalone: true,
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css',
  imports: [ReactiveFormsModule, CommonModule],
})

export class ProductForm implements OnInit {
  productForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    categoryId: new FormControl('', [Validators.required]),
    images: new FormControl<File[]>([])
  });


  get name() { return this.productForm.get("name") }
  get description() { return this.productForm.get("description") }
  get categoryId() { return this.productForm.get("categoryId") }
  get images() { return this.productForm.get("images") }

  get isSubmitted() { return this.submitted }

  submitted = false;
  errorMessage: string | null = null
  successMessage: string | null = null
  createdProductId: number | null = null

  private productService = inject(ProductService)
  categories: Category[] = []

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.productService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: () => {
        this.errorMessage = "Failed to load categories.";
      }
    });
  }

  onFileSelect(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      this.images?.setValue(Array.from(fileList));
    } else {
      this.images?.setValue([]);
  }
}


  onSubmit() {
    this.submitted = true;
    if (this.productForm.valid) {
      this.errorMessage = null;
      this.successMessage = null;

      const productData: CreateProductRequest = {
        name: this.productForm.value.name!,
        description: this.productForm.value.description!,
        categoryId: Number(this.productForm.value.categoryId!),
      };

      this.productService.createProduct(productData)
        .subscribe({
          next: (response) => {
            const productId = parseInt(response.split(' ')[response.split(' ').length - 1]);
            this.createdProductId = productId;

            const images = this.images?.value ?? [];
            if (images.length > 0) {
              this.productService.uploadImages(productId, images).subscribe({
                next: () => {
                  this.successMessage = "Product created successfully with images!";
                  this.productForm.reset();
                  this.submitted = false;
                },
                error: () => {
                  this.successMessage = "Product created successfully, but image upload failed.";
                  this.submitted = false;
                }
              });
            } else {
              this.successMessage = "Product created successfully!";
              this.productForm.reset();
              this.submitted = false;
            }
          },
          error: () => {
            this.errorMessage = "Failed to create product. Please try again.";
            this.submitted = false;
          }
        });
    }
  }
}
