import { Injector, OnInit, EventEmitter } from '@angular/core';
import { ControlValueAccessor, NgControl } from "@angular/forms";
export declare class FormItem implements ControlValueAccessor, OnInit {
    private injector;
    required: boolean;
    readonly: boolean;
    clcChange: EventEmitter<any>;
    innerValue: any;
    control: NgControl;
    onTouchedCallback: () => void;
    onChangeCallback: (_: any) => void;
    constructor(injector: Injector);
    ngOnInit(): void;
    value: any;
    writeValue(value: any): void;
    registerOnChange(fn: any): void;
    registerOnTouched(fn: any): void;
    isInvalid(): boolean;
}
