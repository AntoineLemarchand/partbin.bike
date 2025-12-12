import { Injectable, signal } from '@angular/core';

@Injectable()
export class VtabService {
  selectedId = signal<string>('')
}

