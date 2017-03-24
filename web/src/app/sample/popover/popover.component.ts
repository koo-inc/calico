import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-popover',
  templateUrl: './popover.component.html',
})
export class PopoverComponent implements OnInit {

  constructor(
  ) { }

  ngOnInit() {
  }

  test():void {
    console.log('test')
  }
  log(v:any): void {
    console.log(v);
  }
}
