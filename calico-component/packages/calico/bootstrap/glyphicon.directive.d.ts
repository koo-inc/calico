import { OnInit, ElementRef, Renderer } from '@angular/core';
export declare class GlyphiconDirective implements OnInit {
    private el;
    private renderer;
    glyphicon: string;
    constructor(el: ElementRef, renderer: Renderer);
    ngOnInit(): void;
}
