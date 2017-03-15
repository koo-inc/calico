import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SharedModule } from "../common/shared.module";
import { LayoutNormal } from '../common/layout/layout-normal/layout-normal.component';
import { TopComponent } from './top.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutNormal,
    children: [
      {path: "", component: TopComponent}
    ]
  },
];

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
  ],
  declarations: [
    TopComponent
  ]
})
export class TopModule { }
