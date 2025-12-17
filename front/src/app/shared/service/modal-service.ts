import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  private openModals = new BehaviorSubject<Set<string>>(new Set());
  openModals$ = this.openModals.asObservable();

  open(modalId: string) {
    const current = new Set(this.openModals.value);
    current.add(modalId);
    this.openModals.next(current);
  }

  close(modalId: string) {
    const current = new Set(this.openModals.value);
    current.delete(modalId);
    this.openModals.next(current);
  }

  isOpen(modalId: string): boolean {
    return this.openModals.value.has(modalId);
  }
}