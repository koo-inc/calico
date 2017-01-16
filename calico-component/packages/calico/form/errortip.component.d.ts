import { ElementRef, AfterContentChecked, Renderer } from '@angular/core';
import { NgControl } from "@angular/forms";
import { MessageConfig } from "../util/api.service";
export declare class ErrorTipComponent implements AfterContentChecked {
    private messages;
    private el;
    private renderer;
    target: NgControl;
    constructor(messages: MessageConfig, el: ElementRef, renderer: Renderer);
    ngAfterContentChecked(): void;
    display(): string;
    excited(): boolean;
    getKeys(): string[];
}
