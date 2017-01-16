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
import { Component, forwardRef, Injector, Input } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormItem } from "./item";
var CheckboxComponent = CheckboxComponent_1 = (function (_super) {
    __extends(CheckboxComponent, _super);
    function CheckboxComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.trueValue = true;
        _this.falseValue = false;
        _this.innerChecked = false;
        return _this;
    }
    Object.defineProperty(CheckboxComponent.prototype, "checked", {
        get: function () {
            return this.innerChecked;
        },
        set: function (checked) {
            if (checked !== this.checked) {
                this.innerChecked = checked;
                this.value = checked ? this.trueValue : this.falseValue;
            }
        },
        enumerable: true,
        configurable: true
    });
    CheckboxComponent.prototype.toggle = function () {
        this.checked = !this.checked;
    };
    CheckboxComponent.prototype.writeValue = function (value) {
        _super.prototype.writeValue.call(this, value);
        this.checked = value == this.trueValue;
    };
    return CheckboxComponent;
}(FormItem));
__decorate([
    Input(),
    __metadata("design:type", String)
], CheckboxComponent.prototype, "label", void 0);
__decorate([
    Input(),
    __metadata("design:type", Object)
], CheckboxComponent.prototype, "trueValue", void 0);
__decorate([
    Input(),
    __metadata("design:type", Object)
], CheckboxComponent.prototype, "falseValue", void 0);
CheckboxComponent = CheckboxComponent_1 = __decorate([
    Component({
        selector: 'c-checkbox',
        template: "\n    <button type=\"button\" class=\"btn btn-default c-checkbox\"\n      [class.invalid]=\"isInvalid()\"\n      [class.active]=\"checked\"\n      (click)=\"toggle()\"\n    >{{label}}</button>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
        styles: ["\n    :host {\n      display: inline-block;\n      position: relative;\n    }\n    :host:not(:hover) c-error-tip {\n      display: none !important;\n    }\n  "],
        providers: [
            {
                provide: NG_VALUE_ACCESSOR,
                useExisting: forwardRef(function () { return CheckboxComponent_1; }),
                multi: true
            }
        ]
    }),
    __metadata("design:paramtypes", [Injector])
], CheckboxComponent);
export { CheckboxComponent };
var CheckboxComponent_1;
//# sourceMappingURL=checkbox.component.js.map