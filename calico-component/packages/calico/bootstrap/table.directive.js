var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Directive, ElementRef, Renderer, Input } from '@angular/core';
var ListTableDirective = (function () {
    function ListTableDirective(el, renderer) {
        this.el = el;
        this.renderer = renderer;
        this.hover = true;
        this.striped = true;
        renderer.setElementClass(el.nativeElement, "table", true);
        renderer.setElementClass(el.nativeElement, "table-bordered", true);
        renderer.setElementClass(el.nativeElement, "table-list", true);
    }
    ListTableDirective.prototype.ngOnInit = function () {
        this.renderer.setElementClass(this.el.nativeElement, "table-hover", this.hover);
        this.renderer.setElementClass(this.el.nativeElement, "table-striped", this.striped);
    };
    ListTableDirective.prototype.ngOnChanges = function (changes) {
        this.renderer.setElementClass(this.el.nativeElement, "table-hover", this.hover);
        this.renderer.setElementClass(this.el.nativeElement, "table-striped", this.striped);
    };
    return ListTableDirective;
}());
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], ListTableDirective.prototype, "hover", void 0);
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], ListTableDirective.prototype, "striped", void 0);
ListTableDirective = __decorate([
    Directive({
        selector: '[c-list-table]'
    }),
    __metadata("design:paramtypes", [ElementRef, Renderer])
], ListTableDirective);
export { ListTableDirective };
var InfoTableDirective = (function () {
    function InfoTableDirective(el, renderer) {
        renderer.setElementClass(el.nativeElement, "table", true);
        renderer.setElementClass(el.nativeElement, "table-bordered", true);
        renderer.setElementClass(el.nativeElement, "table-info", true);
    }
    return InfoTableDirective;
}());
InfoTableDirective = __decorate([
    Directive({
        selector: '[c-info-table]'
    }),
    __metadata("design:paramtypes", [ElementRef, Renderer])
], InfoTableDirective);
export { InfoTableDirective };
//# sourceMappingURL=table.directive.js.map