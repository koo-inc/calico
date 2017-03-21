import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SharedModule } from "../common/shared.module";
import { MainService } from "./main.service";
import { LayoutNormal } from '../common/layout/layout-normal/layout-normal.component';
import { IndexComponent } from './index/index.component';
import { EditModalComponent } from "./edit-modal/edit-modal.component";

const routes: Routes = [
  {
    path: '',
    component: LayoutNormal,
    children: [
      {path: "index", component: IndexComponent},
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
    EditModalComponent,
  ],
  providers: [
    MainService,
  ]
})
export class MainModule { }

