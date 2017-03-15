var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
import { Component, Input, ElementRef, Renderer, Inject } from '@angular/core';
import { NgControl, FormControlName } from "@angular/forms";
import { MESSAGE_CONFIG, MessageConfig } from "../util/api.service";
var ErrorTipComponent = (function () {
    function ErrorTipComponent(messages, el, renderer) {
        this.messages = messages;
        this.el = el;
        this.renderer = renderer;
    }
    ErrorTipComponent.prototype.ngAfterContentChecked = function () {
        this.renderer.setElementStyle(this.el.nativeElement, "display", this.display());
    };
    ErrorTipComponent.prototype.display = function () {
        if (!this.excited())
            return "none";
        return "block";
    };
    ErrorTipComponent.prototype.excited = function () {
        return this.target && this.target.errors
            && (!(this.target instanceof FormControlName) || this.target.formDirective.submitted);
    };
    ErrorTipComponent.prototype.getKeys = function () {
        if (this.target == null)
            return [];
        return Object.keys(this.target.errors);
    };
    return ErrorTipComponent;
}());
__decorate([
    Input('for'),
    __metadata("design:type", NgControl)
], ErrorTipComponent.prototype, "target", void 0);
ErrorTipComponent = __decorate([
    Component({
        selector: 'c-error-tip',
        template: "\n    <div *ngIf=\"excited()\">\n      <div *ngFor=\"let key of getKeys()\">{{messages[key] || key}}</div>\n    </div>\n  ",
        styles: ["\n    :host {\n      display: block;\n      bottom: calc(100% + 12px);\n      position: absolute;\n      padding: .5em 1em .4em;\n      background-color: #f66;\n      border: 1px solid #f00;\n      border-radius: 5px;\n      color: #fff;\n      z-index: 10;\n      white-space: nowrap;\n    }\n    :host:after, :host:before {\n      content: '';\n      position: absolute;\n      top: 100%;\n      left: 15px;\n      border: solid transparent;\n    }\n    :host:after {\n      margin-left: 1px;\n      border-top-color: #f66;\n      border-width: 7px;\n    }\n    :host:before {\n      border-top-color: #f00;\n      border-width: 8px;\n    }\n  "]
    }),
    __param(0, Inject(MESSAGE_CONFIG)),
    __metadata("design:paramtypes", [MessageConfig, ElementRef, Renderer])
], ErrorTipComponent);
export { ErrorTipComponent };
//# sourceMappingURL=errortip.component.js.map