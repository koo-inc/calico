var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Directive, Input, ElementRef, Renderer } from '@angular/core';
var ButtonDirective = (function () {
    function ButtonDirective(el, renderer) {
        this.el = el;
        this.renderer = renderer;
        renderer.setElementClass(el.nativeElement, "btn", true);
    }
    ButtonDirective.prototype.ngOnInit = function () {
        var _this = this;
        if (this.buttonType == null)
            return;
        this.buttonType.split(/[,\s]+/).forEach(function (type) {
            if (type.trim() == null)
                return;
            _this.renderer.setElementClass(_this.el.nativeElement, "btn-" + type.trim(), true);
        });
    };
    return ButtonDirective;
}());
__decorate([
    Input('c-btn'),
    __metadata("design:type", String)
], ButtonDirective.prototype, "buttonType", void 0);
ButtonDirective = __decorate([
    Directive({
        selector: '[c-btn]',
    }),
    __metadata("design:paramtypes", [ElementRef, Renderer])
], ButtonDirective);
export { ButtonDirective };
//# sourceMappingURL=button.directive.js.map