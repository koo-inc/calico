import { Component, forwardRef, Injector, Input, OnChanges, SimpleChanges } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormItem } from "./item";

@Component({
  selector: 'c-radios',
  template: `
    <span class="c-radios btn-group" [class.invalid]="isInvalid()">
      <button *ngFor="let e of innerOptions" 
        type="button"
        class="btn btn-default c-radio"
        [class.active]="e.selected"
        (click)="click(e)"
      >{{e.label}}</button>
    </span>
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
      useExisting: forwardRef(() => RadiosComponent),
      multi: true
    }
  ]
})
export class RadiosComponent extends FormItem implements OnChanges {
  constructor(injector: Injector) {
    super(injector);
  }

  @Input() options: any[] = [];
  @Input() optionKey: string = 'id';
  @Input() optionLabel: string = 'name';
  @Input() optionValue: string = null;
  @Input() nullOption: boolean = false;
  @Input() nullOptionLabel: string = '----';

  private innerOptions: RadioOption[];

  ngOnInit(): void {
    super.ngOnInit();
    this.initOptions();
  }

  writeValue(value: any): void {
    super.writeValue(value);
    let key = this.getOptionKey(value);
    this.setSelected(key);
  }

  private click(option: RadioOption) {
    this.setSelected(option.key);
    this.value = option.value;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(Object.has(changes, 'options')
        || Object.has(changes, 'optionLabel')
        || Object.has(changes, 'nullOption')
        || Object.has(changes, 'nullOptionLabel')
    ){
      this.initOptions();
      this.setSelected(this.value);
    }
  }

  private initOptions() {
    this.innerOptions = [];
    if(this.nullOption){
      this.innerOptions.push({ key: null, label: this.nullOptionLabel, value: null, selected: false });
    }
    if(this.options == null || this.options.length == 0){
      return;
    }
    for(let option of this.options){
      let key = this.getOptionKey(option);
      let label = this.getOptionLabel(option);
      let value = this.getOptionValue(option);
      this.innerOptions.push({ key: key, label: label, value: value, selected: false });
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

  private setSelected(key: any){
    this.innerOptions.forEach((option) => {
      option.selected = option.key == key;
    });
  }

}

interface RadioOption {
  key: any;
  label: string;
  value: any;
  selected: boolean;
}
