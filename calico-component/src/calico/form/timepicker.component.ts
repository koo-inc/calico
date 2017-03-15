import { Component, forwardRef, Injector, Input, ViewChild } from '@angular/core';
import {NG_VALUE_ACCESSOR} from '@angular/forms';
import { FormItem } from "./item";

@Component({
  selector: 'c-timepicker',
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
      <div class="c-timepicker-popover" (mousedown)="keep($event)">
        <timepicker [(ngModel)]="timepickerValue"
          [arrowkeys]="true"
          [mousewheel]="false"
          [showSpinners]="true"
          [showMeridian]="false"
          [hourStep]="stepHour"
          [minuteStep]="stepMinute"
        ></timepicker>
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
      this.innerTimepickerValue = d;
      this.value = d != null ? d.toISOString() : null;
      this.popover.show();
    }
  }
  isInvalidText(): boolean {
    return this.innerTextValue != null && this.innerTextValue != '' && this.timepickerValue == null;
  }
  adjustTextValue(): void {
    if(!this.textChanged) return;
    if(this.timepickerValue != null){
      this.innerTextValue = this.formatDate(this.timepickerValue);
    }
    this.textChanged = false;
  }

  innerTimepickerValue: Date;

  get timepickerValue(): Date {
    return this.innerTimepickerValue;
  }
  set timepickerValue(value: Date) {
    if (!Object.isEqual(value, this.innerTimepickerValue)) {
      this.innerTimepickerValue = value;
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
      this.innerTimepickerValue = null;
    }else if(Object.isDate(value)){
      this.innerTimepickerValue = value;
    }else if(!Object.isDate(value)){
      this.innerTimepickerValue = this.toDate(value);
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
    return value.format('{HH}:{mm}');
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
}
