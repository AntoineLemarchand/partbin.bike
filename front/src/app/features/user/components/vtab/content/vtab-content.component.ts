import { Component, computed, inject, input } from "@angular/core";
import { VtabService } from "../vtab.service";

@Component({
  selector: 'app-vtab-content',
  standalone: true,
  template: `
    <div [class.selected]="isVisible()">
      <ng-content></ng-content>
    </div>
  `,
  styleUrl: './vtab-content.component.css',
})
export class VtabContent {
  id = input<string>('');
  vtabs = inject(VtabService);
  isVisible = computed(() => this.vtabs.selectedId() === this.id());
}

