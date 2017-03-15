import { Injector } from '@angular/core';
import { FormItem } from "./item";
import 'moment/locale/ja';
export declare class DatepickerComponent extends FormItem {
    constructor(injector: Injector);
    defaultDate: Date;
    minDate: Date;
    maxDate: Date;
    placeholder: string;
    disabled: any;
    innerTextValue: string;
    textChanged: boolean;
    textValue: string;
    isInvalidText(): boolean;
    adjustTextValue(): void;
    innerDatepickerValue: Date;
    datepickerValue: Date;
    writeValue(value: any): void;
    private toDate(value);
    private formatDate(value);
    popover: any;
    keepFlag: boolean;
    keep(): void;
    onClick(): void;
    onFocus(): void;
    onBlur($event: any): void;
    selectionDone(): void;
}
