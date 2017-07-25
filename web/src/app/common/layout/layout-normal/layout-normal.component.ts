import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'layout-normal',
  template: `
    <app-header></app-header>
    <div class="container-fluid">
      <div class="main">
        <div class="container">
          <router-outlet></router-outlet>
        </div>
      </div>
    </div>
  `
})
export class LayoutNormal implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
