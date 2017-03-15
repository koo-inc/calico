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
var DatepickerComponent = DatepickerComponent_1 = (function (_super) {
    __extends(DatepickerComponent, _super);
    function DatepickerComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.locale = DatepickerComponent_1.locale;
        _this.defaultDate = new Date();
        _this.inline = false;
        _this.yearRange = '{0}:{1}'.format(Date.create().getFullYear() - 20, Date.create().getFullYear() + 20);
        return _this;
    }
    Object.defineProperty(DatepickerComponent.prototype, "calendarValue", {
        get: function () {
            return this.innerCalendarValue;
        },
        set: function (value) {
            if (value !== this.innerCalendarValue) {
                this.innerCalendarValue = value;
                this.value = value != null ? value.toISOString() : null;
            }
        },
        enumerable: true,
        configurable: true
    });
    DatepickerComponent.prototype.writeValue = function (value) {
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
    return DatepickerComponent;
}(FormItem));
DatepickerComponent.locale = {
    firstDayOfWeek: 1,
    dayNames: ["日曜", "月曜", "火曜", "水曜", "木曜", "金曜", "土曜"],
    dayNamesShort: ["日", "月", "火", "水", "木", "金", "土"],
    dayNamesMin: ["日", "月", "火", "水", "木", "金", "土"],
    monthNames: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
    monthNamesShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]
};
__decorate([
    Input(),
    __metadata("design:type", Date)
], DatepickerComponent.prototype, "defaultDate", void 0);
__decorate([
    Input(),
    __metadata("design:type", Date)
], DatepickerComponent.prototype, "minDate", void 0);
__decorate([
    Input(),
    __metadata("design:type", Date)
], DatepickerComponent.prototype, "maxDate", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], DatepickerComponent.prototype, "placeholder", void 0);
__decorate([
    Input(),
    __metadata("design:type", Object)
], DatepickerComponent.prototype, "disabled", void 0);
__decorate([
    Input(),
    __metadata("design:type", Boolean)
], DatepickerComponent.prototype, "inline", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], DatepickerComponent.prototype, "yearRange", void 0);
DatepickerComponent = DatepickerComponent_1 = __decorate([
    Component({
        selector: 'c-datepicker',
        template: "\n    <p-calendar [(ngModel)]=\"calendarValue\"\n      [class.invalid]=\"isInvalid()\"\n      [dateFormat]=\"'yy/mm/dd'\"\n      [monthNavigator]=\"true\"\n      [yearNavigator]=\"true\"\n      [locale]=\"locale\"\n      [defaultDate]=\"defaultDate\"\n      [minDate]=\"minDate\"\n      [maxDate]=\"maxDate\"\n      [placeholder]=\"placeholder\"\n      [disabled]=\"disabled\"\n      [inline]=\"inline\"\n      [yearRange]=\"yearRange\"\n    ></p-calendar>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
        styles: ["\n    :host {\n      display: inline-block;\n      position: relative;\n    }\n    :host:not(:hover) c-error-tip {\n      display: none !important;\n    }\n  "],
        providers: [
            {
                provide: NG_VALUE_ACCESSOR,
                useExisting: forwardRef(function () { return DatepickerComponent_1; }),
                multi: true
            }
        ]
    }),
    __metadata("design:paramtypes", [Injector])
], DatepickerComponent);
export { DatepickerComponent };
var DatepickerComponent_1;
//# sourceMappingURL=datepicker.component.js.map