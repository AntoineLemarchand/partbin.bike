import { AfterContentInit, Component, ContentChildren, inject, QueryList } from "@angular/core";
import { VtabService } from "./vtab.service";
import { VtabItem } from "./item/vtab-item.component";

@Component({
  selector: 'app-vtab',
  standalone: true,
  templateUrl: './vtab.component.html',
  styleUrl: './vtab.component.css',
  providers: [VtabService]
})
export class Vtab implements AfterContentInit {
  vtabs = inject(VtabService);
  @ContentChildren(VtabItem) items!: QueryList<VtabItem>;

  ngAfterContentInit(): void {
      this.vtabs.selectedId.set(this.items.first.contentId());
  }
}

