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
import { Component, forwardRef, Injector, Input, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormItem } from "./item";
var TimepickerComponent = TimepickerComponent_1 = (function (_super) {
    __extends(TimepickerComponent, _super);
    function TimepickerComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.defaultDate = Date.create('00:00');
        _this.stepHour = 1;
        _this.stepMinute = 5;
        _this.textChanged = false;
        _this.keepFlag = false;
        return _this;
    }
    Object.defineProperty(TimepickerComponent.prototype, "textValue", {
        get: function () {
            return this.innerTextValue;
        },
        set: function (value) {
            if (value !== this.innerTextValue) {
                this.textChanged = true;
                this.popover.hide();
                this.innerTextValue = value;
                var d = this.toDate(this.innerTextValue);
                this.innerTimepickerValue = d;
                this.value = d != null ? d.toISOString() : null;
                this.popover.show();
            }
        },
        enumerable: true,
        configurable: true
    });
    TimepickerComponent.prototype.isInvalidText = function () {
        return this.innerTextValue != null && this.innerTextValue != '' && this.timepickerValue == null;
    };
    TimepickerComponent.prototype.adjustTextValue = function () {
        if (!this.textChanged)
            return;
        if (this.timepickerValue != null) {
            this.innerTextValue = this.formatDate(this.timepickerValue);
        }
        this.textChanged = false;
    };
    Object.defineProperty(TimepickerComponent.prototype, "timepickerValue", {
        get: function () {
            return this.innerTimepickerValue;
        },
        set: function (value) {
            if (!Object.isEqual(value, this.innerTimepickerValue)) {
                this.innerTimepickerValue = value;
                this.innerTextValue = this.formatDate(value);
                this.textChanged = false;
                this.value = value != null ? value.toISOString() : null;
            }
        },
        enumerable: true,
        configurable: true
    });
    TimepickerComponent.prototype.writeValue = function (value) {
        _super.prototype.writeValue.call(this, value);
        if (value == null || value == '') {
            this.innerTextValue = null;
        }
        else if (Object.isDate(value)) {
            this.innerTextValue = this.formatDate(value);
        }
        else if (!Object.isDate(value)) {
            this.innerTextValue = this.formatDate(this.toDate(value));
        }
        this.textChanged = false;
        if (value == null || value == '') {
            this.innerTimepickerValue = null;
        }
        else if (Object.isDate(value)) {
            this.innerTimepickerValue = value;
        }
        else if (!Object.isDate(value)) {
            this.innerTimepickerValue = this.toDate(value);
        }
    };
    TimepickerComponent.prototype.toDate = function (value) {
        var d = Date.create(value);
        return d == 'Invalid Date' ? null : d;
    };
    TimepickerComponent.prototype.formatDate = function (value) {
        if (value == null) {
            return null;
        }
        return value.format('{HH}:{mm}');
    };
    TimepickerComponent.prototype.keep = function () {
        this.keepFlag = true;
    };
    TimepickerComponent.prototype.onClick = function () {
        this.popover.show();
    };
    TimepickerComponent.prototype.onFocus = function () {
        this.popover.show();
    };
    TimepickerComponent.prototype.onBlur = function ($event) {
        if (this.keepFlag) {
            $event.target.focus();
            this.keepFlag = false;
        }
        else {
            this.popover.hide();
        }
        this.adjustTextValue();
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
    __metadata("design:type", Object)
], TimepickerComponent.prototype, "disabled", void 0);
__decorate([
    ViewChild('popover'),
    __metadata("design:type", Object)
], TimepickerComponent.prototype, "popover", void 0);
TimepickerComponent = TimepickerComponent_1 = __decorate([
    Component({
        selector: 'c-timepicker',
        template: "\n    <span class=\"text-container\">\n      <input type=\"text\" [(ngModel)]=\"textValue\"\n        [class.invalid]=\"isInvalid()\"\n        #popover=\"bs-popover\"\n        [popover]=\"popoverTpl\"\n        placement=\"bottom\"\n        container=\"body\"\n        triggers=\"\"\n        (focus)=\"onFocus($event)\"\n        (blur)=\"onBlur($event)\"\n        (click)=\"onClick($event)\"\n      ><span class=\"invalid-text glyphicon glyphicon-warning-sign\"\n        [class.active]=\"isInvalidText()\"\n      ></span>\n    </span>\n    <template #popoverTpl>\n      <div class=\"c-timepicker-popover\" (mousedown)=\"keep($event)\">\n        <timepicker [(ngModel)]=\"timepickerValue\"\n          [arrowkeys]=\"true\"\n          [mousewheel]=\"false\"\n          [showSpinners]=\"true\"\n          [showMeridian]=\"false\"\n          [hourStep]=\"stepHour\"\n          [minuteStep]=\"stepMinute\"\n        ></timepicker>\n      </div>\n    </template>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
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