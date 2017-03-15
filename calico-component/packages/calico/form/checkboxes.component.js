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
var CheckboxesComponent = CheckboxesComponent_1 = (function (_super) {
    __extends(CheckboxesComponent, _super);
    function CheckboxesComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.options = [];
        _this.optionKey = 'id';
        _this.optionLabel = 'name';
        _this.optionValue = null;
        return _this;
    }
    CheckboxesComponent.prototype.ngOnInit = function () {
        _super.prototype.ngOnInit.call(this);
        this.initOptions();
    };
    CheckboxesComponent.prototype.writeValue = function (value) {
        _super.prototype.writeValue.call(this, value);
        this.setSelected(value);
    };
    CheckboxesComponent.prototype.click = function (option) {
        option.selected = !option.selected;
        this.value = this.getValues();
    };
    CheckboxesComponent.prototype.ngOnChanges = function (changes) {
        if (Object.has(changes, 'options')
            || Object.has(changes, 'optionLabel')) {
            this.initOptions();
            this.setSelected(this.value);
        }
    };
    CheckboxesComponent.prototype.initOptions = function () {
        this.innerOptions = [];
        if (this.options == null || this.options.length == 0) {
            return;
        }
        for (var _i = 0, _a = this.options; _i < _a.length; _i++) {
            var option = _a[_i];
            var key = this.getOptionKey(option);
            var label = this.getOptionLabel(option);
            var value = this.getOptionValue(option);
            this.innerOptions.push({ key: key, label: label, value: value, selected: false });
        }
    };
    CheckboxesComponent.prototype.getOptionKey = function (option) {
        if (this.optionKey == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionKey];
    };
    CheckboxesComponent.prototype.getOptionLabel = function (option) {
        if (this.optionLabel == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionLabel];
    };
    CheckboxesComponent.prototype.getOptionValue = function (option) {
        if (this.optionValue == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionValue];
    };
    CheckboxesComponent.prototype.getValues = function () {
        return this.innerOptions
            .filter(function (e) { return e.selected; })
            .map(function (e) { return e.value; });
    };
    CheckboxesComponent.prototype.setSelected = function (value) {
        var _this = this;
        if (value == null || !Object.isArray(value) || value.length == 0) {
            return;
        }
        value
            .map(function (e) { return _this.getOptionKey(e); })
            .map(function (key) { return _this.innerOptions.find(function (e) { return e.key == key; }); })
            .filter(function (option) { return option != null; })
            .forEach(function (option) { option.selected = true; });
    };
    return CheckboxesComponent;
}(FormItem));
__decorate([
    Input(),
    __metadata("design:type", Array)
], CheckboxesComponent.prototype, "options", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], CheckboxesComponent.prototype, "optionKey", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], CheckboxesComponent.prototype, "optionLabel", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], CheckboxesComponent.prototype, "optionValue", void 0);
CheckboxesComponent = CheckboxesComponent_1 = __decorate([
    Component({
        selector: 'c-checkboxes',
        template: "\n    <span class=\"btn-group c-checkboxes\" [class.invalid]=\"isInvalid()\">\n      <button *ngFor=\"let e of innerOptions\" \n        type=\"button\"\n        class=\"btn btn-default c-checkbox\"\n        [class.active]=\"e.selected\"\n        (click)=\"click(e)\"\n      >{{e.label}}</button>\n    </span>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
        styles: ["\n    :host {\n      display: inline-block;\n      position: relative;\n    }\n    :host:not(:hover) c-error-tip {\n      display: none !important;\n    }\n  "],
        providers: [
            {
                provide: NG_VALUE_ACCESSOR,
                useExisting: forwardRef(function () { return CheckboxesComponent_1; }),
                multi: true
            }
        ]
    }),
    __metadata("design:paramtypes", [Injector])
], CheckboxesComponent);
export { CheckboxesComponent };
var CheckboxesComponent_1;
//# sourceMappingURL=checkboxes.component.js.map