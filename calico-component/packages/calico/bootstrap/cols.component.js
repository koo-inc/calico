var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component, Input } from '@angular/core';
var ColsComponent = (function () {
    function ColsComponent() {
        this._cols = [];
    }
    Object.defineProperty(ColsComponent.prototype, "cols", {
        get: function () {
            return this._cols;
        },
        set: function (value) {
            if (value == null || !Object.isArray(value) || value.length == 0) {
                this._cols = [];
                return;
            }
            this._cols = value.map(function (e) {
                if (e == null)
                    return 'auto';
                if (Object.isNumber(e))
                    return e + '%';
                return e.toString();
            });
        },
        enumerable: true,
        configurable: true
    });
    return ColsComponent;
}());
__decorate([
    Input('c-cols'),
    __metadata("design:type", Array),
    __metadata("design:paramtypes", [Array])
], ColsComponent.prototype, "cols", null);
ColsComponent = __decorate([
    Component({
        selector: '[c-cols]',
        template: "\n    <col *ngFor=\"let col of cols\" width=\"{{col}}\">\n    <ng-content></ng-content>\n  "
    })
], ColsComponent);
export { ColsComponent };
//# sourceMappingURL=cols.component.js.map