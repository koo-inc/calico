import { Injector, OnChanges, SimpleChanges } from '@angular/core';
import { FormItem } from "./item";
export declare class CheckboxesComponent extends FormItem implements OnChanges {
    constructor(injector: Injector);
    options: any[];
    optionKey: string;
    optionLabel: string;
    optionValue: string;
    private innerOptions;
    ngOnInit(): void;
    writeValue(value: any): void;
    private click(option);
    ngOnChanges(changes: SimpleChanges): void;
    private initOptions();
    private getOptionKey(option);
    private getOptionLabel(option);
    private getOptionValue(option);
    private getValues();
    private setSelected(value);
}
