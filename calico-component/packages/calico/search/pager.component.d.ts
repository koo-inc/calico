import { OnInit, OnDestroy } from "@angular/core";
import { SearchContext } from "./search.service";
import { Subscription } from "rxjs";
export declare class PagerComponent implements OnInit, OnDestroy {
    private searchContext;
    constructor(searchContext: SearchContext);
    info: PageInfo;
    perPageOptions: any[];
    range: number;
    _subscriptions: Subscription[];
    subscription: Subscription;
    ngOnInit(): void;
    ngOnDestroy(): void;
    private initPageInfo();
    private getRecordCount();
    private getPerPage();
    private getCurrentPageNo();
    private getMinPageNo();
    private getMaxPageNo();
    private getHasPrevPage();
    private getHasNextPage();
    private getPageNos();
    onChangePerPage(): void;
    moveTo(no: number): void;
    moveToPrev(): void;
    moveToNext(): void;
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
