import { Injector } from '@angular/core';
import { FormItem } from "./item";
export declare class DatepickerComponent extends FormItem {
    constructor(injector: Injector);
    static locale: {
        firstDayOfWeek: number;
        dayNames: string[];
        dayNamesShort: string[];
        dayNamesMin: string[];
        monthNames: string[];
        monthNamesShort: string[];
    };
    locale: {
        firstDayOfWeek: number;
        dayNames: string[];
        dayNamesShort: string[];
        dayNamesMin: string[];
        monthNames: string[];
        monthNamesShort: string[];
    };
    defaultDate: Date;
    minDate: Date;
    maxDate: Date;
    placeholder: string;
    disabled: any;
    inline: boolean;
    yearRange: string;
    innerCalendarValue: Date;
    calendarValue: Date;
    writeValue(value: any): void;
}
