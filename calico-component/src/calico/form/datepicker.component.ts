import { Component, forwardRef, Injector, Input } from '@angular/core';
import {NG_VALUE_ACCESSOR} from '@angular/forms';
import { FormItem } from "./item";

@Component({
  selector: 'c-datepicker',
  template: `
    <p-calendar [(ngModel)]="calendarValue"
      [class.invalid]="isInvalid()"
      [dateFormat]="'yy/mm/dd'"
      [monthNavigator]="true"
      [yearNavigator]="true"
      [locale]="locale"
      [defaultDate]="defaultDate"
      [minDate]="minDate"
      [maxDate]="maxDate"
      [placeholder]="placeholder"
      [disabled]="disabled"
      [inline]="inline"
      [yearRange]="yearRange"
    ></p-calendar>
    <c-error-tip [for]="control"></c-error-tip>
  `,
  styles: [`
    :host {
      display: inline-block;
      position: relative;
    }
    :host:not(:hover) c-error-tip {
      display: none !important;
    }
  `],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DatepickerComponent),
      multi: true
    }
  ]
})
export class DatepickerComponent extends FormItem {
  constructor(injector: Injector) {
    super(injector);
  }

  static locale = {
    firstDayOfWeek: 1,
    dayNames: ["日曜", "月曜", "火曜", "水曜", "木曜", "金曜", "土曜"],
    dayNamesShort: ["日", "月", "火", "水", "木", "金", "土"],
    dayNamesMin: ["日", "月", "火", "水", "木", "金", "土"],
    monthNames: [ "1","2","3","4","5","6","7","8","9","10","11","12" ],
    monthNamesShort: [ "1","2","3","4","5","6","7","8","9","10","11","12" ]
  };
  locale = DatepickerComponent.locale;

  @Input() defaultDate: Date = new Date();
  @Input() minDate: Date;
  @Input() maxDate: Date;
  @Input() placeholder: string;
  @Input() disabled: any;
  @Input() inline: boolean = false;
  @Input() yearRange: string = '{0}:{1}'.format(Date.create().getFullYear() - 20, Date.create().getFullYear() + 20);

  innerCalendarValue: Date;

  get calendarValue(): Date {
    return this.innerCalendarValue;
  }
  set calendarValue(value: Date) {
    if (value !== this.innerCalendarValue) {
      this.innerCalendarValue = value;
      this.value = value != null ? value.toISOString() : null;
    }
  }

  writeValue(value: any): void {
    super.writeValue(value);
    if (value !== this.innerCalendarValue) {
      if(value == null || value == ''){
        value = null;
      }else if(!Object.isDate(value)){
        value = Date.create(value);
      }
      this.innerCalendarValue = value;
    }
  }
}
