import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-select-single',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './select-single.component.html',
  styleUrls: ['./select-single.component.scss']
})
export class SelectSingleComponent {
  @Input() items: { selectedId: number, label: string }[] = [];
  @Input() selectedId: number | null = null;
  @Input() label: string = '';
  @Input() placeholder: string = 'Selectează...';
  @Output() selectedChange = new EventEmitter<number>();

  isOpen = false;
  searchTerm = '';
  
  get filteredItems() {
    if (!Array.isArray(this.items)) return [];
    return this.items.filter(i =>
      typeof i.label === 'string' &&
      i.label.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }
  
  

  toggle() {
    this.isOpen = !this.isOpen;
  }

  selectItem(id: number) {
    console.log('ID selectat:', id);
    this.selectedId = id;
    this.selectedChange.emit(id);
    this.isOpen = false;
    this.searchTerm = '';
  }
  
  get selectedLabel(): string | null {
    const item = this.items.find(i => i.selectedId === this.selectedId);
    return item?.label || null;
  }
  
  

  clearSelection() {
    this.selectedId = null;
    this.selectedChange.emit(undefined);
    this.searchTerm = '';
  }

  
  
}
