import { Injector, OnChanges, SimpleChanges } from '@angular/core';
import { FormItem } from "./item";
export declare class RadiosComponent extends FormItem implements OnChanges {
    constructor(injector: Injector);
    options: any[];
    optionKey: string;
    optionLabel: string;
    optionValue: string;
    nullOption: boolean;
    nullOptionLabel: string;
    private innerOptions;
    ngOnInit(): void;
    writeValue(value: any): void;
    private click(option);
    ngOnChanges(changes: SimpleChanges): void;
    private initOptions();
    private getOptionKey(option);
    private getOptionLabel(option);
    private getOptionValue(option);
    private setSelected(key);
}
