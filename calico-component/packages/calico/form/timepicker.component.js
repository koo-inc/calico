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
var TimepickerComponent = TimepickerComponent_1 = (function (_super) {
    __extends(TimepickerComponent, _super);
    function TimepickerComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.defaultDate = Date.create('00:00');
        _this.stepHour = 1;
        _this.stepMinute = 5;
        _this.inline = false;
        return _this;
    }
    Object.defineProperty(TimepickerComponent.prototype, "calendarValue", {
        get: function () {
            return this.innerCalendarValue;
        },
        set: function (value) {
            // if (value !== this.innerCalendarValue) {
            this.innerCalendarValue = value;
            this.value = value != null ? value.toISOString() : null;
            // }
        },
        enumerable: true,
        configurable: true
    });
    TimepickerComponent.prototype.writeValue = function (value) {
        _super.prototype.writeValue.call(this, value);
        if (value !== this.innerCalendarValue) {
            if (value == null || value == '') {
                value = null;
            }
            else if (!Object.isDate(value)) {
                value = Date.create(value);
            }
            this.innerCalendarValue = value;
        }
    };
    return TimepickerComponent;
}(FormItem));
__decorate([
    Input(),
    __metadata("design:type", Date)
], TimepickerComponent.prototype, "defaultDate", void 0);
__decorate([
    Input(),
    __metadata("design:type", Number)
], TimepickerComponent.prototype, "stepHour", void 0);
__decorate([
    Input(),
    __metadata("design:type", Number)
], TimepickerComponent.prototype, "stepMinute", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], TimepickerComponent.prototype, "placeholder", void 0);
__decorate([
    Input(),
    __metadata("design:type", Object)
], TimepickerComponent.prototype, "disabled", void 0);
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], TimepickerComponent.prototype, "inline", void 0);
TimepickerComponent = TimepickerComponent_1 = __decorate([
    Component({
        selector: 'c-timepicker',
        template: "\n    <p-calendar [(ngModel)]=\"calendarValue\"\n      [class.invalid]=\"isInvalid()\"\n      [timeOnly]=\"true\"\n      [defaultDate]=\"defaultDate\"\n      [stepHour]=\"stepHour\"\n      [stepMinute]=\"stepMinute\"\n      [placeholder]=\"placeholder\"\n      [disabled]=\"disabled\"\n      [inline]=\"inline\"\n    ></p-calendar>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
        styles: ["\n    :host {\n      display: inline-block;\n      position: relative;\n    }\n    :host:not(:hover) c-error-tip {\n      display: none !important;\n    }\n  "],
        providers: [
            {
                provide: NG_VALUE_ACCESSOR,
                useExisting: forwardRef(function () { return TimepickerComponent_1; }),
                multi: true
            }
        ]
    }),
    __metadata("design:paramtypes", [Injector])
], TimepickerComponent);
export { TimepickerComponent };
var TimepickerComponent_1;
//# sourceMappingURL=timepicker.component.js.map