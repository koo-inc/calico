import { Component, forwardRef, Injector, Input } from '@angular/core';
import {NG_VALUE_ACCESSOR} from '@angular/forms';
import { FormItem } from "./item";

@Component({
  selector: 'c-timepicker',
  template: `
    <p-calendar [(ngModel)]="calendarValue"
      [class.invalid]="isInvalid()"
      [timeOnly]="true"
      [defaultDate]="defaultDate"
      [stepHour]="stepHour"
      [stepMinute]="stepMinute"
      [placeholder]="placeholder"
      [disabled]="disabled"
      [inline]="inline"
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
      useExisting: forwardRef(() => TimepickerComponent),
      multi: true
    }
  ]
})
export class TimepickerComponent extends FormItem {
  constructor(injector: Injector) {
    super(injector);
  }

  @Input() defaultDate: Date = Date.create('00:00');
  @Input() stepHour: number = 1;
  @Input() stepMinute: number = 5;
  @Input() placeholder: string;
  @Input() disabled: any;
  @Input() inline: boolean = false;

  innerCalendarValue: Date;

  get calendarValue(): Date {
    return this.innerCalendarValue;
  }
  set calendarValue(value: Date) {
    // if (value !== this.innerCalendarValue) {
      this.innerCalendarValue = value;
      this.value = value != null ? value.toISOString() : null;
    // }
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
