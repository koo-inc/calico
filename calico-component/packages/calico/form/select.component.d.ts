import { Injector, OnChanges, SimpleChanges } from '@angular/core';
import { FormItem } from "./item";
export declare class SelectComponent extends FormItem implements OnChanges {
    constructor(injector: Injector);
    options: any[];
    optionKey: string;
    optionLabel: string;
    optionValue: string;
    nullOption: boolean;
    nullOptionLabel: string;
    private innerSelectValue;
    private innerOptions;
    ngOnInit(): void;
    writeValue(value: any): void;
    selectValue: any;
    trackBy(idx: any, option: any): any;
    ngOnChanges(changes: SimpleChanges): void;
    private initOptions();
    private getOptionKey(option);
    private getOptionLabel(option);
    private getOptionValue(option);
}
