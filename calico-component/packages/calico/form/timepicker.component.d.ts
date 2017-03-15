import { Injector } from '@angular/core';
import { FormItem } from "./item";
export declare class TimepickerComponent extends FormItem {
    constructor(injector: Injector);
    defaultDate: Date;
    stepHour: number;
    stepMinute: number;
    disabled: any;
    innerTextValue: string;
    textChanged: boolean;
    textValue: string;
    isInvalidText(): boolean;
    adjustTextValue(): void;
    innerTimepickerValue: Date;
    timepickerValue: Date;
    writeValue(value: any): void;
    private toDate(value);
    private formatDate(value);
    popover: any;
    keepFlag: boolean;
    keep(): void;
    onClick(): void;
    onFocus(): void;
    onBlur($event: any): void;
}
