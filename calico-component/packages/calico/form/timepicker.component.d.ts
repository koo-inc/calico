import { Injector } from '@angular/core';
import { FormItem } from "./item";
export declare class TimepickerComponent extends FormItem {
    constructor(injector: Injector);
    defaultDate: Date;
    stepHour: number;
    stepMinute: number;
    placeholder: string;
    disabled: any;
    inline: boolean;
    innerCalendarValue: Date;
    calendarValue: Date;
    writeValue(value: any): void;
}
