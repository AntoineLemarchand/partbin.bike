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
    categoryId: new FormControl('', [Validators.required])
  })

  get name() { return this.productForm.get("name") }
  get description() { return this.productForm.get("description") }
  get categoryId() { return this.productForm.get("categoryId") }

  get isSubmitted() { return this.submitted }

  submitted = false;
  errorMessage: string | null = null
  successMessage: string | null = null

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
          next: () => {
            this.successMessage = "Product created successfully!";
            this.productForm.reset();
            this.submitted = false;
          },
          error: () => {
            this.errorMessage = "Failed to create product. Please try again.";
            this.submitted = false;
          }
        });
    }
  }
}
