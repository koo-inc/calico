import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SharedModule } from "../common/shared.module";
import { ValidationService } from "./validation/validation.service";
import { LayoutNormal } from '../common/layout/layout-normal/layout-normal.component';
import { IndexComponent } from './index/index.component';
import { ValidationComponent } from './validation/validation.component';
import { PopoverComponent } from "./popover/popover.component";

const routes: Routes = [
  {
    path: '',
    component: LayoutNormal,
    children: [
      {path: "", component: IndexComponent},
      {path: "validation", component: ValidationComponent},
      {path: "popover", component: PopoverComponent},
    ]
  },
];

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
  ],
  declarations: [
    IndexComponent,
    ValidationComponent,
    PopoverComponent,
  ],
  providers: [
    ValidationService,
  ]
})
export class MainModule { }

