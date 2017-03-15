import { Injector } from '@angular/core';
import { FormItem } from "./item";
export declare class CheckboxComponent extends FormItem {
    constructor(injector: Injector);
    label: string;
    trueValue: any;
    falseValue: any;
    innerChecked: boolean;
    checked: boolean;
    toggle(): void;
    writeValue(value: any): void;
}
