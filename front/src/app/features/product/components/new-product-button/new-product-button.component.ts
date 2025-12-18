import { Component } from "@angular/core";
import { ModalButton } from "../../../../../app/shared/components/modal/modal-button.component";

@Component({
  selector: 'app-new-product-button',
  standalone: true,
  template: `
    <app-modal-button modalId="new-product-modal">
      <button class="new-product-btn">+ Create New Product</button>
    </app-modal-button>
  `,
  styleUrl: './new-product-button.component.css',
  imports: [ModalButton],
})

export class NewProductButton {
}