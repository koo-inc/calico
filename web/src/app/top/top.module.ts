import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SharedModule } from "../common/shared.module";
import { TopComponent } from './top.component';
import { buildRoute } from "app/app.routing";

const routes: Routes = [
  buildRoute({
    children: [
      {path: "", component: TopComponent}
    ],
  }),
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
