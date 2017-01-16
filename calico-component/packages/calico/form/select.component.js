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
var SelectComponent = SelectComponent_1 = (function (_super) {
    __extends(SelectComponent, _super);
    function SelectComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.options = [];
        _this.optionKey = 'id';
        _this.optionLabel = 'name';
        _this.optionValue = null;
        _this.nullOption = true;
        _this.nullOptionLabel = '----';
        return _this;
    }
    SelectComponent.prototype.ngOnInit = function () {
        _super.prototype.ngOnInit.call(this);
        this.initOptions();
    };
    SelectComponent.prototype.writeValue = function (value) {
        _super.prototype.writeValue.call(this, value);
        this.innerSelectValue = this.getOptionKey(value);
    };
    Object.defineProperty(SelectComponent.prototype, "selectValue", {
        get: function () {
            return this.innerSelectValue;
        },
        set: function (key) {
            if (key !== this.innerSelectValue) {
                this.innerSelectValue = key;
                var option = this.innerOptions.find(function (e) { return e.key == key; });
                this.value = option != null ? option.value : null;
            }
        },
        enumerable: true,
        configurable: true
    });
    SelectComponent.prototype.trackBy = function (idx, option) {
        return option.key;
    };
    SelectComponent.prototype.ngOnChanges = function (changes) {
        if (Object.has(changes, 'options')
            || Object.has(changes, 'optionLabel')
            || Object.has(changes, 'nullOption')
            || Object.has(changes, 'nullOptionLabel')) {
            this.initOptions();
            this.innerSelectValue = this.getOptionKey(this.value);
        }
    };
    SelectComponent.prototype.initOptions = function () {
        this.innerOptions = [];
        if (this.nullOption) {
            this.innerOptions.push({ key: null, label: this.nullOptionLabel, value: null });
        }
        if (this.options == null || this.options.length == 0) {
            return;
        }
        for (var _i = 0, _a = this.options; _i < _a.length; _i++) {
            var option = _a[_i];
            var key = this.getOptionKey(option);
            var label = this.getOptionLabel(option);
            var value = this.getOptionValue(option);
            this.innerOptions.push({ key: key, label: label, value: value });
        }
    };
    SelectComponent.prototype.getOptionKey = function (option) {
        if (this.optionKey == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionKey];
    };
    SelectComponent.prototype.getOptionLabel = function (option) {
        if (this.optionLabel == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionLabel];
    };
    SelectComponent.prototype.getOptionValue = function (option) {
        if (this.optionValue == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionValue];
    };
    return SelectComponent;
}(FormItem));
__decorate([
    Input(),
    __metadata("design:type", Array)
], SelectComponent.prototype, "options", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], SelectComponent.prototype, "optionKey", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], SelectComponent.prototype, "optionLabel", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], SelectComponent.prototype, "optionValue", void 0);
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], SelectComponent.prototype, "nullOption", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], SelectComponent.prototype, "nullOptionLabel", void 0);
SelectComponent = SelectComponent_1 = __decorate([
    Component({
        selector: 'c-select',
        template: "\n    <select [(ngModel)]=\"selectValue\"\n      class=\"c-select\"\n      [class.invalid]=\"isInvalid()\"\n    >\n      <option *ngFor=\"let e of innerOptions;trackBy:trackBy\" [ngValue]=\"e.key\">{{e.label}}</option>\n    </select>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
        styles: ["\n    :host {\n      display: inline-block;\n      position: relative;\n    }\n    :host:not(:hover) c-error-tip {\n      display: none !important;\n    }\n  "],
        providers: [
            {
                provide: NG_VALUE_ACCESSOR,
                useExisting: forwardRef(function () { return SelectComponent_1; }),
                multi: true
            }
        ]
    }),
    __metadata("design:paramtypes", [Injector])
], SelectComponent);
export { SelectComponent };
var SelectComponent_1;
//# sourceMappingURL=select.component.js.map