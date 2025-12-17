import { Component } from '@angular/core';
import { ModalButton } from '../../../shared/components/modal/modal-button.component';
import { matPlusOutline } from '@ng-icons/material-icons/outline';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { Modal } from '../../../shared/components/modal/modal.component';

@Component({
  selector: 'app-home',
  standalone: true,
  template: `
    <h1>homepage</h1>
    <div class="modal-button">
      <app-modal-button modalId="new-product-modal">New product <ng-icon name="matPlusOutline" /></app-modal-button>
    </div>
    <app-modal modalId="new-product-modal">
      <h2 id="modal-title">Create New Product</h2>
      <p>This is where your product creation form would go.</p>
      <button>Save Product</button>
    </app-modal>
  `,
  styleUrl: "./home.page.css",
  imports: [ModalButton, NgIcon, Modal],
  viewProviders: [provideIcons({matPlusOutline})]
})
export class HomePage {}

