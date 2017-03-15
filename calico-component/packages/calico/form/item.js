var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Input, Output, EventEmitter } from '@angular/core';
import { FormControlName, NgControl } from "@angular/forms";
var noop = function () { };
var FormItem = (function () {
    function FormItem(injector) {
        this.injector = injector;
        this.required = false;
        this.readonly = false;
        this.clcChange = new EventEmitter();
        this.onTouchedCallback = noop;
        this.onChangeCallback = noop;
    }
    FormItem.prototype.ngOnInit = function () {
        this.control = this.injector.get(NgControl);
    };
    Object.defineProperty(FormItem.prototype, "value", {
        get: function () {
            return this.innerValue;
        },
        set: function (value) {
            if (value !== this.innerValue) {
                this.innerValue = value;
                this.onChangeCallback(value);
                this.clcChange.emit(value);
            }
        },
        enumerable: true,
        configurable: true
    });
    FormItem.prototype.writeValue = function (value) {
        if (value !== this.innerValue) {
            this.innerValue = value;
        }
    };
    FormItem.prototype.registerOnChange = function (fn) {
        this.onChangeCallback = fn;
    };
    FormItem.prototype.registerOnTouched = function (fn) {
        this.onTouchedCallback = fn;
    };
    FormItem.prototype.isInvalid = function () {
        return this.control && this.control.invalid
            && (!(this.control instanceof FormControlName) || this.control.formDirective.submitted);
    };
    return FormItem;
}());
export { FormItem };
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], FormItem.prototype, "required", void 0);
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], FormItem.prototype, "readonly", void 0);
__decorate([
    Output(),
    __metadata("design:type", EventEmitter)
], FormItem.prototype, "clcChange", void 0);
//# sourceMappingURL=item.js.map