import { ElementRef, Renderer, OnInit, OnChanges, SimpleChanges } from '@angular/core';
export declare class ListTableDirective implements OnInit, OnChanges {
    private el;
    private renderer;
    constructor(el: ElementRef, renderer: Renderer);
    hover: boolean;
    striped: boolean;
    ngOnInit(): void;
    ngOnChanges(changes: SimpleChanges): void;
}
export declare class InfoTableDirective {
    constructor(el: ElementRef, renderer: Renderer);
}
