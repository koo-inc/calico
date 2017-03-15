import { OnInit, ElementRef, Renderer } from '@angular/core';
export declare class ButtonDirective implements OnInit {
    private el;
    private renderer;
    buttonType: string;
    constructor(el: ElementRef, renderer: Renderer);
    ngOnInit(): void;
}
