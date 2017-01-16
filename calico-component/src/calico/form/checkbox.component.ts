import { Component, forwardRef, Injector, Input } from '@angular/core';
import {NG_VALUE_ACCESSOR} from '@angular/forms';
import {FormItem} from "./item";

@Component({
  selector: 'c-checkbox',
  template: `
    <button type="button" class="btn btn-default c-checkbox"
      [class.invalid]="isInvalid()"
      [class.active]="checked"
      (click)="toggle()"
    >{{label}}</button>
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
      useExisting: forwardRef(() => CheckboxComponent),
      multi: true
    }
  ]
})
export class CheckboxComponent extends FormItem {
  constructor(injector: Injector) {
    super(injector);
  }

  @Input() label: string;
  @Input() trueValue: any = true;
  @Input() falseValue: any = false;

  innerChecked: boolean = false;

  get checked(): boolean {
    return this.innerChecked;
  }
  set checked(checked: boolean) {
    if (checked !== this.checked) {
      this.innerChecked = checked;
      this.value = checked ? this.trueValue : this.falseValue;
    }
  }

  toggle(){
    this.checked = !this.checked;
  }

  writeValue(value: any): void {
    super.writeValue(value);
    this.checked = value == this.trueValue;
  }

}
