import { Component, Input } from "@angular/core";
import { ModalService } from "../../service/modal-service";

@Component({
  selector: 'app-modal-button',
  standalone: true,
  template: `
    <button (click)="openModal()" [attr.aria-controls]="modalId" aria-haspopup="dialog">
      <ng-content></ng-content>
    </button>
  `,
})

export class ModalButton {
  @Input() modalId: string = ""

  constructor(private modalService: ModalService) {}

  openModal() {
    if (this.modalId) {
      this.modalService.open(this.modalId);
    }
  }
}
