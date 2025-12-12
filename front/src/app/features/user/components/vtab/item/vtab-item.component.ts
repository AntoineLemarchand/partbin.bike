import { Component, inject, input, output } from "@angular/core";
import { VtabService } from "../vtab.service";

@Component({
  selector: 'app-vtab-item',
  standalone: true,
  template: '<button (click)="selectTab()">{{name()}}</button>',
  styleUrl: './vtab-item.component.css',
})
export class VtabItem {
  name = input('')
  contentId = input('')
  onSelected = output<string>();
  vtabs = inject(VtabService);

  selectTab() {
    this.vtabs.selectedId.set(this.contentId());
  }
}
