import { Component, Input } from "@angular/core";

@Component({
  selector: 'app-modal-button',
  standalone: true,
  template: `
    <button (click)="openModal()"><ng-content></ng-content></button>
  `,
})

export class ModalButton {
  @Input() modalId: string = ""

  openModal() {
    console.log(this.modalId)
  }
}
