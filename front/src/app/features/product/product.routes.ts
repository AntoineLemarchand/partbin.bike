import { Routes } from '@angular/router';
import { ProductPageComponent } from './pages/product/product.page';

export const productRoutes: Routes = [
  {
    path: ':id',
    component: ProductPageComponent,
    title: 'Product Details'
  }
];