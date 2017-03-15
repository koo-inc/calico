import { Component, Input, ElementRef, Renderer } from '@angular/core';

@Component({
  selector: '[c-panel]',
  template: `
    <div class="panel-heading">{{heading}}</div>
    <div class="panel-body">
      <ng-content></ng-content>
    </div>
  `
})
export class PanelComponent {
  @Input('c-panel') heading: string;

  constructor(el: ElementRef, renderer: Renderer) {
    renderer.setElementClass(el.nativeElement, "panel", true);
    renderer.setElementClass(el.nativeElement, "panel-default", true);
  }
}
