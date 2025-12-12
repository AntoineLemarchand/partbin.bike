import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { ProfileForm } from '../../components/profile-form/profile-form.component';
import { VtabItem } from '../../components/vtab/item/vtab-item.component';
import { Vtab } from '../../components/vtab/vtab.component';
import { VtabContent } from '../../components/vtab/content/vtab-content.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.page.html',
  styleUrl: './profile.page.css',
  imports: [ProfileForm, Vtab, VtabItem, VtabContent]
})
export class ProfilePage {
    private route = inject(ActivatedRoute);
    private data = toSignal(this.route.data);
    user = computed(() => this.data()!['user']);
}
