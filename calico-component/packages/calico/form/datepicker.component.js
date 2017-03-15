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
// https://github.com/valor-software/ng2-bootstrap/issues/455
import 'moment/locale/ja';
import * as moment from 'moment';
moment.locale('ja');
var DatepickerComponent = DatepickerComponent_1 = (function (_super) {
    __extends(DatepickerComponent, _super);
    function DatepickerComponent(injector) {
        var _this = _super.call(this, injector) || this;
        _this.defaultDate = new Date();
        _this.textChanged = false;
        _this.keepFlag = false;
        return _this;
    }
    Object.defineProperty(DatepickerComponent.prototype, "textValue", {
        get: function () {
            return this.innerTextValue;
        },
        set: function (value) {
            if (value !== this.innerTextValue) {
                this.textChanged = true;
                this.popover.hide();
                this.innerTextValue = value;
                var d = this.toDate(this.innerTextValue);
                this.innerDatepickerValue = d;
                this.value = d != null ? d.toISOString() : null;
                this.popover.show();
            }
        },
        enumerable: true,
        configurable: true
    });
    DatepickerComponent.prototype.isInvalidText = function () {
        return this.innerTextValue != null && this.innerTextValue != '' && this.datepickerValue == null;
    };
    DatepickerComponent.prototype.adjustTextValue = function () {
        if (!this.textChanged)
            return;
        if (this.datepickerValue != null) {
            this.innerTextValue = this.formatDate(this.datepickerValue);
        }
        this.textChanged = false;
    };
    Object.defineProperty(DatepickerComponent.prototype, "datepickerValue", {
        get: function () {
            return this.innerDatepickerValue;
        },
        set: function (value) {
            if (!Object.isEqual(value, this.innerDatepickerValue)) {
                this.innerDatepickerValue = value;
                this.innerTextValue = this.formatDate(value);
                this.textChanged = false;
                this.value = value != null ? value.toISOString() : null;
            }
        },
        enumerable: true,
        configurable: true
    });
    DatepickerComponent.prototype.writeValue = function (value) {
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
            this.innerDatepickerValue = null;
        }
        else if (Object.isDate(value)) {
            this.innerDatepickerValue = value;
        }
        else if (!Object.isDate(value)) {
            this.innerDatepickerValue = this.toDate(value);
        }
    };
    DatepickerComponent.prototype.toDate = function (value) {
        var d = Date.create(value);
        return d == 'Invalid Date' ? null : d;
    };
    DatepickerComponent.prototype.formatDate = function (value) {
        if (value == null) {
            return null;
        }
        return value.format('{yyyy}/{MM}/{dd}');
    };
    DatepickerComponent.prototype.keep = function () {
        this.keepFlag = true;
    };
    DatepickerComponent.prototype.onClick = function () {
        this.popover.show();
    };
    DatepickerComponent.prototype.onFocus = function () {
        this.popover.show();
    };
    DatepickerComponent.prototype.onBlur = function ($event) {
        if (this.keepFlag) {
            $event.target.focus();
            this.keepFlag = false;
        }
        else {
            this.popover.hide();
        }
        this.adjustTextValue();
    };
    DatepickerComponent.prototype.selectionDone = function () {
        var _this = this;
        setTimeout(function () {
            _this.popover.hide();
        });
    };
    return DatepickerComponent;
}(FormItem));
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
    ViewChild('popover'),
    __metadata("design:type", Object)
], DatepickerComponent.prototype, "popover", void 0);
DatepickerComponent = DatepickerComponent_1 = __decorate([
    Component({
        selector: 'c-datepicker',
        template: "\n    <span class=\"text-container\">\n      <input type=\"text\" [(ngModel)]=\"textValue\"\n        [class.invalid]=\"isInvalid()\"\n        #popover=\"bs-popover\"\n        [popover]=\"popoverTpl\"\n        placement=\"bottom\"\n        container=\"body\"\n        triggers=\"\"\n        (focus)=\"onFocus($event)\"\n        (blur)=\"onBlur($event)\"\n        (click)=\"onClick($event)\"\n      ><span class=\"invalid-text glyphicon glyphicon-warning-sign\"\n        [class.active]=\"isInvalidText()\"\n      ></span>\n    </span>\n    <template #popoverTpl>\n      <div class=\"c-datepicker-popover\" (mousedown)=\"keep($event)\">\n        <datepicker [(ngModel)]=\"datepickerValue\"\n          [showWeeks]=\"false\"\n          [activeDate]=\"null\"\n          [startingDay]=\"1\"\n          formatDayTitle=\"YYYY\u5E74MM\u6708\"\n          formatMonth=\"MM\u6708\"\n          formatMonthTitle=\"YYYY\u5E74\"\n          formatYear=\"YYYY\u5E74\"\n          [minDate]=\"minDate\"\n          [maxDate]=\"maxDate\"\n          (selectionDone)=\"selectionDone()\"\n        ></datepicker>\n      </div>\n    </template>\n    <c-error-tip [for]=\"control\"></c-error-tip>\n  ",
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