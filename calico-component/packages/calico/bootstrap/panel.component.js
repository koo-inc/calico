var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component, Input, ElementRef, Renderer } from '@angular/core';
var PanelComponent = (function () {
    function PanelComponent(el, renderer) {
        renderer.setElementClass(el.nativeElement, "panel", true);
        renderer.setElementClass(el.nativeElement, "panel-default", true);
    }
    return PanelComponent;
}());
__decorate([
    Input('c-panel'),
    __metadata("design:type", String)
], PanelComponent.prototype, "heading", void 0);
PanelComponent = __decorate([
    Component({
        selector: '[c-panel]',
        template: "\n    <div class=\"panel-heading\">{{heading}}</div>\n    <div class=\"panel-body\">\n      <ng-content></ng-content>\n    </div>\n  "
    }),
    __metadata("design:paramtypes", [ElementRef, Renderer])
], PanelComponent);
export { PanelComponent };
//# sourceMappingURL=panel.component.js.map