import { Component, Input, OnInit, OnDestroy } from "@angular/core";

import { SearchContext } from "./search.service";
import { Subscription } from "rxjs";

@Component({
  selector: '[c-pager]',
  template: `
    <span class="c-pager">
      <span class="record-count">
        検索結果<span>{{info.recordCount}}</span>件
      </span>
      <c-select [(ngModel)]="info.perPage"
        [options]="perPageOptions"
        optionValue="id"
        [nullOption]="false"
        (change)="onChangePerPage()"></c-select>
      <ul class="pagination  pagination-sm">
        <li [class.disabled]="!info.hasPrevPage">
          <a (click)="moveToPrev()" aria-label="Previous"><span>&laquo;</span></a>
        </li>
        <li *ngFor="let no of info.pageNos"
          [class.active]="info.currentPageNo == no"
          [class.ellipsis]="no == null">
          <a *ngIf="no != null" (click)="moveTo(no)">{{no}}</a>
          <a *ngIf="no == null">...</a>
        </li>
        <li [class.disabled]="!info.hasNextPage">
          <a (click)="moveToNext()" aria-label="Next"><span>&raquo;</span></a>
        </li>
      </ul>
    </span>
  `
})
export class PagerComponent implements OnInit, OnDestroy {

  constructor(
    private searchContext: SearchContext,
  ) {}

  info: PageInfo;

  perPageOptions: any[] = [
    { id: 10, name: '10件' },
    { id: 50, name: '50件' },
    { id: 100, name: '100件' },
  ];
  range: number = 3;

  _subscriptions: Subscription[] = [];
  set subscription(subription: Subscription) {
    this._subscriptions.push(subription);
  }

  ngOnInit(): void {
    this.initPageInfo();
    this.subscription = this.searchContext.searched.subscribe(() => {
      this.initPageInfo();
    });
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(s => s.unsubscribe());
    this._subscriptions = [];
  }

  private initPageInfo(): void {
    this.info = {
      recordCount: this.getRecordCount(),
      perPage: this.getPerPage(),
      currentPageNo: this.getCurrentPageNo(),
      minPageNo: this.getMinPageNo(),
      maxPageNo: this.getMaxPageNo(),
      hasPrevPage: this.getHasPrevPage(),
      hasNextPage: this.getHasNextPage(),
      pageNos: this.getPageNos(),
    };
  }
  private getRecordCount(): number {
    if(this.searchContext.result == null) return 0;
    return this.searchContext.result._count
  }
  private getPerPage(): number {
    return this.searchContext.form.value._page.perPage;
  }
  private getCurrentPageNo(): number {
    return this.searchContext.form.value._page.no;
  }
  private getMinPageNo(): number {
    return 1;
  }
  private getMaxPageNo(): number {
    if(this.searchContext.result == null) return 1;
    return Math.floor(this.getRecordCount() / this.getPerPage())
      + (this.getRecordCount() % this.getPerPage() == 0 ? 0 : 1);
  }
  private getHasPrevPage(): boolean {
    return this.getCurrentPageNo() > this.getMinPageNo();
  }
  private getHasNextPage(): boolean {
    return this.getCurrentPageNo() < this.getMaxPageNo();
  }
  private getPageNos(): number[] {
    let currentPageNo = this.getCurrentPageNo();
    let minPageNo = this.getMinPageNo();
    let maxPageNo = this.getMaxPageNo();
    let ret: number[] = [];
    let i: number;
    for(i = currentPageNo - this.range;i <= currentPageNo + this.range;i++){
      if(minPageNo <= i && i <= maxPageNo) ret.push(i);
    }
    if(ret.indexOf(minPageNo) === -1){
      if(ret.first() >= 3){
        ret.unshift(null);
      }
      ret.unshift(minPageNo);
    }
    if(ret.indexOf(maxPageNo) === -1){
      if(ret.last() <= maxPageNo -2){
        ret.push(null);
      }
      ret.push(maxPageNo);
    }
    return ret;
  }

  onChangePerPage(): void {
    this.searchContext.onPerPageChange(this.info.perPage);
  }

  moveTo(no: number): void {
    this.searchContext.onPageNoChange(no);
  }

  moveToPrev(): void {
    if(!this.info.hasPrevPage) return;
    this.searchContext.onPageNoChange(this.info.currentPageNo - 1);
  }

  moveToNext(): void {
    if(!this.info.hasNextPage) return;
    this.searchContext.onPageNoChange(this.info.currentPageNo + 1);
  }
}

export interface PageInfo {
  recordCount: number;
  perPage: number;
  currentPageNo: number;
  minPageNo: number;
  maxPageNo: number;
  hasPrevPage: boolean;
  hasNextPage: boolean;
  pageNos: number[];
}

