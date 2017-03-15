import { Component, forwardRef, Injector, Input, OnChanges, SimpleChanges } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormItem } from "./item";

@Component({
  selector: 'c-select',
  template: `
    <select [(ngModel)]="selectValue"
      class="c-select"
      [class.invalid]="isInvalid()"
    >
      <option *ngFor="let e of innerOptions;trackBy:trackBy" [ngValue]="e.key">{{e.label}}</option>
    </select>
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
      useExisting: forwardRef(() => SelectComponent),
      multi: true
    }
  ]
})
export class SelectComponent extends FormItem implements OnChanges {
  constructor(injector: Injector) {
    super(injector);
  }

  @Input() options: any[] = [];
  @Input() optionKey: string = 'id';
  @Input() optionLabel: string = 'name';
  @Input() optionValue: string = null;
  @Input() nullOption: boolean = true;
  @Input() nullOptionLabel: string = '----';

  private innerSelectValue: any;
  private innerOptions: SelectOption[];

  ngOnInit(): void {
    super.ngOnInit();
    this.initOptions();
  }

  writeValue(value: any): void {
    super.writeValue(value);
    this.innerSelectValue = this.getOptionKey(value);
  }

  get selectValue(): any {
    return this.innerSelectValue;
  }
  set selectValue(key: any) {
    if (key !== this.innerSelectValue) {
      this.innerSelectValue = key;
      let option = this.innerOptions.find((e) => e.key == key);
      this.value = option != null ? option.value : null;
    }
  }

  trackBy(idx: any, option: any) {
    return option.key;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(Object.has(changes, 'options')
        || Object.has(changes, 'optionLabel')
        || Object.has(changes, 'nullOption')
        || Object.has(changes, 'nullOptionLabel')
    ){
      this.initOptions();
      this.innerSelectValue = this.getOptionKey(this.value);
    }
  }

  private initOptions() {
    this.innerOptions = [];
    if(this.nullOption){
      this.innerOptions.push({ key: null, label: this.nullOptionLabel, value: null });
    }
    if(this.options == null || this.options.length == 0){
      return;
    }
    for(let option of this.options){
      let key = this.getOptionKey(option);
      let label = this.getOptionLabel(option);
      let value = this.getOptionValue(option);
      this.innerOptions.push({ key: key, label: label, value: value });
    }
  }
  private getOptionKey(option: any){
    if(this.optionKey == null || !Object.isObject(option)){
      return option;
    }
    return option[this.optionKey];
  }
  private getOptionLabel(option: any){
    if(this.optionLabel == null || !Object.isObject(option)){
      return option;
    }
    return option[this.optionLabel];
  }
  private getOptionValue(option: any){
    if(this.optionValue == null || !Object.isObject(option)){
      return option;
    }
    return option[this.optionValue];
  }

}

interface SelectOption {
  key: any;
  label: string;
  value: any;
}
