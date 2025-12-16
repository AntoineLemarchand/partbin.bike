import { Component, Input } from "@angular/core";
import { NgIcon, provideIcons } from "@ng-icons/core";
import { matCloseOutline } from "@ng-icons/material-icons/outline";

@Component({
  selector: 'app-modal',
  standalone: true,
  template: `
  <div class="modal-wrapper" [class.opened]="true" id={{modalId}}>
    <button><ng-icon name="matCloseOutline" /></button>
    <div class="modal-content">
      <ng-content></ng-content>
    </div>
  </div>
  `,
  styleUrl: "./modal.component.css",
  imports: [NgIcon],
  viewProviders: [provideIcons({matCloseOutline})]
})

export class Modal {
  @Input() modalId: string = ""
}
