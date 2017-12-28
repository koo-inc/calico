import { Component } from '@angular/core';
import { PopoverDirective } from "calico/ui";

@Component({
  selector: 'app-popover',
  templateUrl: './popover.component.html',
})
export class PopoverComponent {
  value1: string;
  value2: string;

  event: string;

  bomb = false;
  openAndDistinct(p: PopoverDirective) {
    p.open();

    setTimeout(() => this.bomb = true, 3000);
  }
}
