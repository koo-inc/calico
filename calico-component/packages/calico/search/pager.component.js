var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component } from "@angular/core";
import { SearchContext } from "./search.service";
var PagerComponent = (function () {
    function PagerComponent(searchContext) {
        this.searchContext = searchContext;
        this.perPageOptions = [
            { id: 10, name: '10件' },
            { id: 50, name: '50件' },
            { id: 100, name: '100件' },
        ];
        this.range = 3;
        this._subscriptions = [];
    }
    Object.defineProperty(PagerComponent.prototype, "subscription", {
        set: function (subription) {
            this._subscriptions.push(subription);
        },
        enumerable: true,
        configurable: true
    });
    PagerComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.initPageInfo();
        this.subscription = this.searchContext.searched.subscribe(function () {
            _this.initPageInfo();
        });
    };
    PagerComponent.prototype.ngOnDestroy = function () {
        this._subscriptions.forEach(function (s) { return s.unsubscribe(); });
        this._subscriptions = [];
    };
    PagerComponent.prototype.initPageInfo = function () {
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
    };
    PagerComponent.prototype.getRecordCount = function () {
        if (this.searchContext.result == null)
            return 0;
        return this.searchContext.result._count;
    };
    PagerComponent.prototype.getPerPage = function () {
        return this.searchContext.form.value._page.perPage;
    };
    PagerComponent.prototype.getCurrentPageNo = function () {
        return this.searchContext.form.value._page.no;
    };
    PagerComponent.prototype.getMinPageNo = function () {
        return 1;
    };
    PagerComponent.prototype.getMaxPageNo = function () {
        if (this.searchContext.result == null)
            return 1;
        return Math.floor(this.getRecordCount() / this.getPerPage())
            + (this.getRecordCount() % this.getPerPage() == 0 ? 0 : 1);
    };
    PagerComponent.prototype.getHasPrevPage = function () {
        return this.getCurrentPageNo() > this.getMinPageNo();
    };
    PagerComponent.prototype.getHasNextPage = function () {
        return this.getCurrentPageNo() < this.getMaxPageNo();
    };
    PagerComponent.prototype.getPageNos = function () {
        var currentPageNo = this.getCurrentPageNo();
        var minPageNo = this.getMinPageNo();
        var maxPageNo = this.getMaxPageNo();
        var ret = [];
        var i;
        for (i = currentPageNo - this.range; i <= currentPageNo + this.range; i++) {
            if (minPageNo <= i && i <= maxPageNo)
                ret.push(i);
        }
        if (ret.indexOf(minPageNo) === -1) {
            if (ret.first() >= 3) {
                ret.unshift(null);
            }
            ret.unshift(minPageNo);
        }
        if (ret.indexOf(maxPageNo) === -1) {
            if (ret.last() <= maxPageNo - 2) {
                ret.push(null);
            }
            ret.push(maxPageNo);
        }
        return ret;
    };
    PagerComponent.prototype.onChangePerPage = function () {
        this.searchContext.onPerPageChange(this.info.perPage);
    };
    PagerComponent.prototype.moveTo = function (no) {
        this.searchContext.onPageNoChange(no);
    };
    PagerComponent.prototype.moveToPrev = function () {
        if (!this.info.hasPrevPage)
            return;
        this.searchContext.onPageNoChange(this.info.currentPageNo - 1);
    };
    PagerComponent.prototype.moveToNext = function () {
        if (!this.info.hasNextPage)
            return;
        this.searchContext.onPageNoChange(this.info.currentPageNo + 1);
    };
    return PagerComponent;
}());
PagerComponent = __decorate([
    Component({
        selector: '[c-pager]',
        template: "\n    <span class=\"c-pager\">\n      <span class=\"record-count\">\n        \u691C\u7D22\u7D50\u679C<span>{{info.recordCount}}</span>\u4EF6\n      </span>\n      <c-select [(ngModel)]=\"info.perPage\"\n        [options]=\"perPageOptions\"\n        optionValue=\"id\"\n        [nullOption]=\"false\"\n        (change)=\"onChangePerPage()\"></c-select>\n      <ul class=\"pagination  pagination-sm\">\n        <li [class.disabled]=\"!info.hasPrevPage\">\n          <a (click)=\"moveToPrev()\" aria-label=\"Previous\"><span>&laquo;</span></a>\n        </li>\n        <li *ngFor=\"let no of info.pageNos\"\n          [class.active]=\"info.currentPageNo == no\"\n          [class.ellipsis]=\"no == null\">\n          <a *ngIf=\"no != null\" (click)=\"moveTo(no)\">{{no}}</a>\n          <a *ngIf=\"no == null\">...</a>\n        </li>\n        <li [class.disabled]=\"!info.hasNextPage\">\n          <a (click)=\"moveToNext()\" aria-label=\"Next\"><span>&raquo;</span></a>\n        </li>\n      </ul>\n    </span>\n  "
    }),
    __metadata("design:paramtypes", [SearchContext])
], PagerComponent);
export { PagerComponent };
//# sourceMappingURL=pager.component.js.map