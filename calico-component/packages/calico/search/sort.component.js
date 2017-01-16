var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component, Input } from "@angular/core";
import { SearchContext } from "./search.service";
var SortComponent = (function () {
    function SortComponent(searchContext) {
        this.searchContext = searchContext;
    }
    SortComponent.prototype.onClick = function () {
        this.searchContext.onSortChange(this.prop);
    };
    return SortComponent;
}());
__decorate([
    Input('c-sort'),
    __metadata("design:type", String)
], SortComponent.prototype, "prop", void 0);
SortComponent = __decorate([
    Component({
        selector: '[c-sort]',
        template: "\n    <div class=\"c-sort\"\n      [class.asc]=\"searchContext.form.value._sort?.prop == prop && searchContext.form.value._sort?.type == 'ASC'\"\n      [class.desc]=\"searchContext.form.value._sort?.prop == prop && searchContext.form.value._sort?.type == 'DESC'\"\n      (click)=\"onClick()\">\n      <ng-content></ng-content>\n    </div>\n  "
    }),
    __metadata("design:paramtypes", [SearchContext])
], SortComponent);
export { SortComponent };
//# sourceMappingURL=sort.component.js.map