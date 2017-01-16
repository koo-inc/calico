import { Component, Input } from "@angular/core";

import { SearchContext } from "./search.service";

@Component({
  selector: '[c-sort]',
  template: `
    <div class="c-sort"
      [class.asc]="searchContext.form.value._sort?.prop == prop && searchContext.form.value._sort?.type == 'ASC'"
      [class.desc]="searchContext.form.value._sort?.prop == prop && searchContext.form.value._sort?.type == 'DESC'"
      (click)="onClick()">
      <ng-content></ng-content>
    </div>
  `
})
export class SortComponent {
  @Input('c-sort') prop: string;

  constructor(
    private searchContext: SearchContext,
  ) {}

  onClick(): void {
    this.searchContext.onSortChange(this.prop);
  }
}

