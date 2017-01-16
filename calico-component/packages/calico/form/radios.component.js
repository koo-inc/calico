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
var RadiosComponent = RadiosComponent_1 = (function (_super) {
    __extends(RadiosComponent, _super);
    function RadiosComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.options = [];
        _this.optionKey = 'id';
        _this.optionLabel = 'name';
        _this.optionValue = null;
        _this.nullOption = false;
        _this.nullOptionLabel = '----';
        return _this;
    }
    RadiosComponent.prototype.ngOnInit = function () {
        _super.prototype.ngOnInit.call(this);
        this.initOptions();
    };
    RadiosComponent.prototype.writeValue = function (value) {
        _super.prototype.writeValue.call(this, value);
        var key = this.getOptionKey(value);
        this.setSelected(key);
    };
    RadiosComponent.prototype.click = function (option) {
        this.setSelected(option.key);
        this.value = option.value;
    };
    RadiosComponent.prototype.ngOnChanges = function (changes) {
        if (Object.has(changes, 'options')
            || Object.has(changes, 'optionLabel')
            || Object.has(changes, 'nullOption')
            || Object.has(changes, 'nullOptionLabel')) {
            this.initOptions();
            this.setSelected(this.value);
        }
    };
    RadiosComponent.prototype.initOptions = function () {
        this.innerOptions = [];
        if (this.nullOption) {
            this.innerOptions.push({ key: null, label: this.nullOptionLabel, value: null, selected: false });
        }
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
    RadiosComponent.prototype.getOptionKey = function (option) {
        if (this.optionKey == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionKey];
    };
    RadiosComponent.prototype.getOptionLabel = function (option) {
        if (this.optionLabel == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionLabel];
    };
    RadiosComponent.prototype.getOptionValue = function (option) {
        if (this.optionValue == null || !Object.isObject(option)) {
            return option;
        }
        return option[this.optionValue];
    };
    RadiosComponent.prototype.setSelected = function (key) {
        this.innerOptions.forEach(function (option) {
            option.selected = option.key == key;
        });
    };
    return RadiosComponent;
}(FormItem));
__decorate([
    Input(),
    __metadata("design:type", Array)
], RadiosComponent.prototype, "options", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], RadiosComponent.prototype, "optionKey", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], RadiosComponent.prototype, "optionLabel", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], RadiosComponent.prototype, "optionValue", void 0);
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], RadiosComponent.prototype, "nullOption", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], RadiosComponent.prototype, "nullOptionLabel", void 0);
RadiosComponent = RadiosComponent_1 = __decorate([
    Component({
        selector: 'c-radios',
        template: "\n    <span class=\"c-radios btn-group\" [class.invalid]=\"isInvalid()\">\n      <button *ngFor=\"let e of innerOptions\" \n        type=\"button\"\n        class=\"btn btn-default c-radio\"\n        [class.active]=\"e.selected\"\n        (click)=\"click(e)\"\n      >{{e.label}}</button>\n    </span>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
        styles: ["\n    :host {\n      display: inline-block;\n      position: relative;\n    }\n    :host:not(:hover) c-error-tip {\n      display: none !important;\n    }\n  "],
        providers: [
            {
                provide: NG_VALUE_ACCESSOR,
                useExisting: forwardRef(function () { return RadiosComponent_1; }),
                multi: true
            }
        ]
    }),
    __metadata("design:paramtypes", [Injector])
], RadiosComponent);
export { RadiosComponent };
var RadiosComponent_1;
//# sourceMappingURL=radios.component.js.map