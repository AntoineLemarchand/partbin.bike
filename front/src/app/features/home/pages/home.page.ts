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
      <app-modal-button modalId="new-product-modal"><ng-icon name="matPlusOutline" /></app-modal-button>
    </div>
    <app-modal>coucou</app-modal>
  `,
  styleUrl: "./home.page.css",
  imports: [ModalButton, NgIcon, Modal],
  viewProviders: [provideIcons({matPlusOutline})]
})
export class HomePage {}

