import {
  Component, Input, ElementRef, AfterContentChecked, Renderer, Injector, OpaqueToken, Inject
} from '@angular/core';
import { NgControl, FormControlName } from "@angular/forms";
import { MESSAGE_CONFIG, MessageConfig } from "../util/api.service";

@Component({
  selector: 'c-error-tip',
  template: `
    <div *ngIf="excited()">
      <div *ngFor="let key of getKeys()">{{messages[key] || key}}</div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      bottom: calc(100% + 12px);
      position: absolute;
      padding: .5em 1em .4em;
      background-color: #f66;
      border: 1px solid #f00;
      border-radius: 5px;
      color: #fff;
      z-index: 10;
      white-space: nowrap;
    }
    :host:after, :host:before {
      content: '';
      position: absolute;
      top: 100%;
      left: 15px;
      border: solid transparent;
    }
    :host:after {
      margin-left: 1px;
      border-top-color: #f66;
      border-width: 7px;
    }
    :host:before {
      border-top-color: #f00;
      border-width: 8px;
    }
  `]
})
export class ErrorTipComponent implements AfterContentChecked {
  @Input('for') target: NgControl;

  constructor(@Inject(MESSAGE_CONFIG) private messages: MessageConfig, private el: ElementRef, private renderer: Renderer) {
  }

  ngAfterContentChecked(): void {
    this.renderer.setElementStyle(this.el.nativeElement, "display", this.display());
  }

  display(): string {
    if (!this.excited()) return "none";
    return "block";
  }
  excited(): boolean {
    return this.target && this.target.errors
      && (!(this.target instanceof FormControlName) || this.target.formDirective.submitted);
  }

  getKeys(): string[] {
    if (this.target == null) return [];
    return Object.keys(this.target.errors) as string[];
  }
}
