import { Component } from "@angular/core";
import { Modal } from "../../../../../app/shared/components/modal/modal.component";
import { ProductForm } from "../product-form/product-form.component";
import { provideHttpClient } from "@angular/common/http";

@Component({
  selector: 'app-new-product-modal',
  standalone: true,
  template: `
    <app-modal modalId="new-product-modal">
      <h2>Create New Product</h2>
      <app-product-form></app-product-form>
    </app-modal>
  `,
  imports: [Modal, ProductForm],
  
})

export class NewProductModal {
}