import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ProductService, CreateProductRequest } from "../../services/product-service";

@Component({
  selector: 'app-product-form',
  standalone: true,
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css',
  imports: [ReactiveFormsModule],
})

export class ProductForm {
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

  onSubmit() {
    this.submitted = true;
    if (this.productForm.valid) {
      this.errorMessage = null;
      this.successMessage = null;

      const productData: CreateProductRequest = {
        name: this.productForm.value.name!,
        description: this.productForm.value.description!,
        categoryId: 1,
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
