import { Component, computed, inject, input, output } from "@angular/core";
import { VtabService } from "../vtab.service";

@Component({
  selector: 'app-vtab-item',
  standalone: true,
  template: '<button (click)="selectTab()" [disabled]="isSelected()">{{name()}}</button>',
  styleUrl: './vtab-item.component.css',
})
export class VtabItem {
  name = input('')
  contentId = input('')
  onSelected = output<string>();
  vtabs = inject(VtabService);
  isSelected = computed(() => this.vtabs.selectedId() === this.contentId());

  selectTab() {
    this.vtabs.selectedId.set(this.contentId());
  }
}
