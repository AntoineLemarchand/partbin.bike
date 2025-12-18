import { Component, computed, inject, signal, OnInit } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { ProfileForm } from '../../components/profile-form/profile-form.component';
import { VtabItem } from '../../components/vtab/item/vtab-item.component';
import { Vtab } from '../../components/vtab/vtab.component';
import { VtabContent } from '../../components/vtab/content/vtab-content.component';
import { ProductListComponent } from '../../../../shared/components/product-list/product-list.component';
import { ProductService, Product } from '../../../product/services/product-service';

@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.page.html',
  styleUrl: './profile.page.css',
  imports: [ProfileForm, Vtab, VtabItem, VtabContent, ProductListComponent]
})
export class ProfilePage implements OnInit {
    private route = inject(ActivatedRoute);
    private data = toSignal(this.route.data);
    private productService = inject(ProductService);
    
    user = computed(() => this.data()!['user']);
    myProducts = signal<Product[]>([]);
    myProductsLoading = signal(true);
    wishlist = signal<Product[]>([]);
    wishlistLoading = signal(true);

    ngOnInit() {
      this.loadUserProducts();
      this.loadWishlist();
    }

    private loadUserProducts() {
      this.myProductsLoading.set(true);
      this.productService.getMyProducts().subscribe({
        next: (products) => {
          this.myProducts.set(products);
          this.myProductsLoading.set(false);
        },
        error: (error) => {
          console.error('Error loading user products:', error);
          this.myProductsLoading.set(false);
        }
      });
    }

    private loadWishlist() {
      this.wishlistLoading.set(true);
      this.productService.getMyWishlist().subscribe({
        next: (products) => {
          this.wishlist.set(products);
          this.wishlistLoading.set(false);
        },
        error: (error) => {
          console.error('Error loading wishlist:', error);
          this.wishlistLoading.set(false);
        }
      });
    }
}
