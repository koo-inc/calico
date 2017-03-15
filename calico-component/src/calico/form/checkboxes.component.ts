import { Component, forwardRef, Injector, Input, OnChanges, SimpleChanges } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormItem } from "./item";

@Component({
  selector: 'c-checkboxes',
  template: `
    <span class="btn-group c-checkboxes" [class.invalid]="isInvalid()">
      <button *ngFor="let e of innerOptions" 
        type="button"
        class="btn btn-default c-checkbox"
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
      useExisting: forwardRef(() => CheckboxesComponent),
      multi: true
    }
  ]
})
export class CheckboxesComponent extends FormItem implements OnChanges {
  constructor(injector: Injector) {
    super(injector);
  }

  @Input() options: any[] = [];
  @Input() optionKey: string = 'id';
  @Input() optionLabel: string = 'name';
  @Input() optionValue: string = null;

  private innerOptions: CheckboxOption[];

  ngOnInit(): void {
    super.ngOnInit();
    this.initOptions();
  }

  writeValue(value: any): void {
    super.writeValue(value);
    this.setSelected(value);
  }

  private click(option: CheckboxOption) {
    option.selected = !option.selected;
    this.value = this.getValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(Object.has(changes, 'options')
      || Object.has(changes, 'optionLabel')
    ){
      this.initOptions();
      this.setSelected(this.value);
    }
  }

  private initOptions() {
    this.innerOptions = [];
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

  private getValues(): any[] {
    return this.innerOptions
      .filter((e) => e.selected)
      .map((e) => e.value);
  }
  private setSelected(value: any) {
    if(value == null || !Object.isArray(value) || value.length == 0){
      return;
    }
    (<any[]>value)
      .map((e) => this.getOptionKey(e))
      .map((key) => this.innerOptions.find((e) => e.key == key))
      .filter((option) => option != null)
      .forEach((option: CheckboxOption) => {option.selected = true;})
  }

}

interface CheckboxOption {
  key: any;
  label: string;
  value: any;
  selected: boolean;
}
