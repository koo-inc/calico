import { Directive, Input, OnInit, ElementRef, Renderer } from '@angular/core';

@Directive({
  selector: '[c-glyphicon]',
})
export class GlyphiconDirective implements OnInit {
  @Input('c-glyphicon') glyphicon: string;

  constructor(private el: ElementRef, private renderer: Renderer) {
    renderer.setElementClass(el.nativeElement, "glyphicon", true);
  }

  ngOnInit(): void {
    if (this.glyphicon == null) return;
    if (this.glyphicon.match(/^\s*$/)) return;
    this.renderer.setElementClass(this.el.nativeElement, `glyphicon-${this.glyphicon.trim()}`, true);
  }
}
