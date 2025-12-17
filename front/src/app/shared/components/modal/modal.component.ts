import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { NgIcon, provideIcons } from "@ng-icons/core";
import { matCloseOutline } from "@ng-icons/material-icons/outline";
import { ModalService } from "../../service/modal-service";
import { Subscription } from "rxjs";

@Component({
  selector: 'app-modal',
  standalone: true,
  template: `
  <div class="modal-wrapper" [class.opened]="isOpened" [attr.id]="modalId" role="dialog" aria-modal="true" aria-labelledby="modal-title">
    <div class="modal-backdrop" (click)="onBackdropClick()"></div>
    <div class="modal-container">
      <button class="modal-close" (click)="closeModal()" aria-label="Close modal">
        <ng-icon name="matCloseOutline" />
      </button>
      <div class="modal-content">
        <ng-content></ng-content>
      </div>
    </div>
  </div>
  `,
  styleUrl: "./modal.component.css",
  imports: [NgIcon],
  viewProviders: [provideIcons({matCloseOutline})]
})

export class Modal implements OnInit, OnDestroy {
  @Input() modalId: string = ""
  @Input() closable: boolean = true
  @Input() backdropClosable: boolean = true

  isOpened = false;
  private subscription: Subscription = new Subscription();

  constructor(private modalService: ModalService) {}

  ngOnInit() {
    this.subscription = this.modalService.openModals$.subscribe((openModals: Set<string>) => {
      this.isOpened = openModals.has(this.modalId);
      if (this.isOpened) {
        document.addEventListener('keydown', this.onEscapeKey.bind(this));
      } else {
        document.removeEventListener('keydown', this.onEscapeKey.bind(this));
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    document.removeEventListener('keydown', this.onEscapeKey.bind(this));
  }

  closeModal() {
    if (this.closable) {
      this.modalService.close(this.modalId);
    }
  }

  onBackdropClick() {
    if (this.backdropClosable) {
      this.closeModal();
    }
  }

  private onEscapeKey(event: KeyboardEvent) {
    if (event.key === 'Escape') {
      this.closeModal();
    }
  }
}
