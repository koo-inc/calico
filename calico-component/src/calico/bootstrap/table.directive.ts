import { Directive, ElementRef, Renderer, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[c-list-table]'
})
export class ListTableDirective implements OnInit, OnChanges {
  constructor(private el: ElementRef, private renderer: Renderer) {
    renderer.setElementClass(el.nativeElement, "table", true);
    renderer.setElementClass(el.nativeElement, "table-bordered", true);
    renderer.setElementClass(el.nativeElement, "table-list", true);
  }

  @Input() hover: boolean = true;
  @Input() striped: boolean = true;

  ngOnInit(): void {
    this.renderer.setElementClass(this.el.nativeElement, "table-hover", this.hover);
    this.renderer.setElementClass(this.el.nativeElement, "table-striped", this.striped);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.renderer.setElementClass(this.el.nativeElement, "table-hover", this.hover);
    this.renderer.setElementClass(this.el.nativeElement, "table-striped", this.striped);
  }
}

@Directive({
  selector: '[c-info-table]'
})
export class InfoTableDirective {
  constructor(el: ElementRef, renderer: Renderer) {
    renderer.setElementClass(el.nativeElement, "table", true);
    renderer.setElementClass(el.nativeElement, "table-bordered", true);
    renderer.setElementClass(el.nativeElement, "table-info", true);
  }
}
