import { CommonModule } from '@angular/common';
import { Component, ContentChild, EventEmitter, Input, Output, TemplateRef } from '@angular/core';

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent {
  @Input() columns: { key: string; label: string }[] = [];
  @Input() data: any[] = [];
  @Input() actions: string[] = [];
  @Input() rowClassFunction?: (row: any) => string;

  @ContentChild('cell', { static: false }) cellTemplate?: TemplateRef<any>;

  @Output() action = new EventEmitter<{ type: string; row: any }>();
  @Output() sort = new EventEmitter<{ key: string; direction: 'asc' | 'desc' }>();

  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  pageSize: number = 10;
  currentPage: number = 0;

  paginatedData(): any[] {
    const arr = this.data ?? [];
    const startIndex = this.currentPage * this.pageSize;
    return arr.slice(startIndex, startIndex + this.pageSize);
  }

  totalPages(): number {
    const length = (this.data ?? []).length;
    return Math.ceil(length / this.pageSize) || 1;
  }

  nextPage(): void {
    if ((this.currentPage + 1) * this.pageSize < (this.data ?? []).length) {
      this.currentPage++;
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  onSort(key: string): void {
    if (this.sortColumn === key) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = key;
      this.sortDirection = 'asc';
    }
    this.sort.emit({ key: this.sortColumn, direction: this.sortDirection });
  }
}
