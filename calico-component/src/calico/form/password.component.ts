import {Component, ElementRef, Renderer, forwardRef, Injector} from '@angular/core';
import {NG_VALUE_ACCESSOR} from '@angular/forms';
import {FormItem} from "./item";

@Component({
  selector: 'c-password',
  template: `
    <input type="password" [(ngModel)]="value"
      class="c-password"
      [required]="required"
      [readonly]="readonly"
      [class.invalid]="isInvalid()"/>
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
      useExisting: forwardRef(() => PasswordComponent),
      multi: true
    }
  ]
})
export class PasswordComponent extends FormItem {
  constructor(injector: Injector) {
    super(injector);
  }
}
