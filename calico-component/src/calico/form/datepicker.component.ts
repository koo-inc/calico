import { Component, forwardRef, Injector, Input, ViewChild, TemplateRef } from '@angular/core';
import {NG_VALUE_ACCESSOR} from '@angular/forms';
import { FormItem } from "./item";

// https://github.com/valor-software/ng2-bootstrap/issues/455
import 'moment/locale/ja';
import * as moment from 'moment';
moment.locale('ja');

@Component({
  selector: 'c-datepicker',
  template: `
    <span class="text-container">
      <input type="text" [(ngModel)]="textValue"
        [class.invalid]="isInvalid()"
        #popover="bs-popover"
        [popover]="popoverTpl"
        placement="bottom"
        container="body"
        triggers=""
        (focus)="onFocus($event)"
        (blur)="onBlur($event)"
        (click)="onClick($event)"
      ><span class="invalid-text glyphicon glyphicon-warning-sign"
        [class.active]="isInvalidText()"
      ></span>
    </span>
    <template #popoverTpl>
      <div class="c-datepicker-popover" (mousedown)="keep($event)">
        <datepicker [(ngModel)]="datepickerValue"
          [showWeeks]="false"
          [activeDate]="null"
          [startingDay]="1"
          formatDayTitle="YYYY年MM月"
          formatMonth="MM月"
          formatMonthTitle="YYYY年"
          formatYear="YYYY年"
          [minDate]="minDate"
          [maxDate]="maxDate"
          (selectionDone)="selectionDone()"
        ></datepicker>
      </div>
    </template>
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

  @Input() defaultDate: Date = new Date();
  @Input() minDate: Date;
  @Input() maxDate: Date;
  @Input() placeholder: string;
  @Input() disabled: any;

  innerTextValue: string;
  textChanged: boolean = false;

  get textValue(): string {
    return this.innerTextValue;
  }
  set textValue(value: string) {
    if (value !== this.innerTextValue) {
      this.textChanged = true;
      this.popover.hide();
      this.innerTextValue = value;
      let d: Date = this.toDate(this.innerTextValue);
      this.innerDatepickerValue = d;
      this.value = d != null ? d.toISOString() : null;
      this.popover.show();
    }
  }
  isInvalidText(): boolean {
    return this.innerTextValue != null && this.innerTextValue != '' && this.datepickerValue == null;
  }
  adjustTextValue(): void {
    if(!this.textChanged) return;
    if(this.datepickerValue != null){
      this.innerTextValue = this.formatDate(this.datepickerValue);
    }
    this.textChanged = false;
  }

  innerDatepickerValue: Date;

  get datepickerValue(): Date {
    return this.innerDatepickerValue;
  }
  set datepickerValue(value: Date) {
    if (!Object.isEqual(value, this.innerDatepickerValue)) {
      this.innerDatepickerValue = value;
      this.innerTextValue = this.formatDate(value);
      this.textChanged = false;
      this.value = value != null ? value.toISOString() : null;
    }
  }

  writeValue(value: any): void {
    super.writeValue(value);

    if(value == null || value == ''){
      this.innerTextValue = null;
    }else if(Object.isDate(value)){
      this.innerTextValue = this.formatDate(value);
    }else if(!Object.isDate(value)){
      this.innerTextValue = this.formatDate(this.toDate(value));
    }
    this.textChanged = false;

    if(value == null || value == ''){
      this.innerDatepickerValue = null;
    }else if(Object.isDate(value)){
      this.innerDatepickerValue = value;
    }else if(!Object.isDate(value)){
      this.innerDatepickerValue = this.toDate(value);
    }
  }

  private toDate(value: any): Date {
    let d: any = Date.create(value);
    return d == 'Invalid Date' ? null : d;
  }

  private formatDate(value: Date): string {
    if(value == null){
      return null;
    }
    return value.format('{yyyy}/{MM}/{dd}');
  }

  @ViewChild('popover') popover;

  keepFlag = false;

  keep(): void {
    this.keepFlag = true;
  }

  onClick(): void {
    this.popover.show();
  }

  onFocus(): void {
    this.popover.show();
  }

  onBlur($event): void {
    if(this.keepFlag){
      $event.target.focus();
      this.keepFlag = false;
    }else{
      this.popover.hide();
    }
    this.adjustTextValue();
  }

  selectionDone(): void {
    setTimeout(() => {
      this.popover.hide();
    });
  }
}
