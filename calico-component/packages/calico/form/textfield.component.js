var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component, forwardRef, Injector } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormItem } from "./item";
var TextFieldComponent = TextFieldComponent_1 = (function (_super) {
    __extends(TextFieldComponent, _super);
    function TextFieldComponent(injector) {
        return _super.call(this, injector) || this;
    }
    return TextFieldComponent;
}(FormItem));
TextFieldComponent = TextFieldComponent_1 = __decorate([
    Component({
        selector: 'c-textfield',
        template: "\n    <input type=\"text\" [(ngModel)]=\"value\"\n      class=\"c-textfield\"\n      [readonly]=\"readonly\"\n      [class.invalid]=\"isInvalid()\"/>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
        styles: ["\n    :host {\n      display: inline-block;\n      position: relative;\n    }\n    :host:not(:hover) c-error-tip {\n      display: none !important;\n    }\n  "],
        providers: [
            {
                provide: NG_VALUE_ACCESSOR,
                useExisting: forwardRef(function () { return TextFieldComponent_1; }),
                multi: true
            }
        ]
    }),
    __metadata("design:paramtypes", [Injector])
], TextFieldComponent);
export { TextFieldComponent };
var TextFieldComponent_1;
//# sourceMappingURL=textfield.component.js.map