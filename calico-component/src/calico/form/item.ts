import { Input, Injector, OnInit, Output, EventEmitter } from '@angular/core';
import { ControlValueAccessor, FormControlName, NgControl } from "@angular/forms";

const noop = () => {};

export class FormItem implements ControlValueAccessor, OnInit {
  @Input() required: boolean = false;
  @Input() readonly: boolean = false;

  @Output() clcChange: EventEmitter<any> = new EventEmitter();

  innerValue: any;
  control: NgControl;
  onTouchedCallback = noop;
  onChangeCallback: (_: any) => void = noop;

  constructor(private injector: Injector) {
  }

  ngOnInit(): void {
    this.control = this.injector.get(NgControl);
  }

  get value(): any {
    return this.innerValue;
  }
  set value(value: any) {
    if (value !== this.innerValue) {
      this.innerValue = value;
      this.onChangeCallback(value);
      this.clcChange.emit(value);
    }
  }

  writeValue(value: any): void {
    if (value !== this.innerValue) {
      this.innerValue = value;
    }
  }

  registerOnChange(fn: any): void {
    this.onChangeCallback = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouchedCallback = fn;
  }

  isInvalid(): boolean {
    return this.control && this.control.invalid
      && (!(this.control instanceof FormControlName) || this.control.formDirective.submitted);
  }
}
