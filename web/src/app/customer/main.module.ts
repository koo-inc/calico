import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SharedModule } from "../common/shared.module";
import { MainService } from "./main.service";
import { LayoutNormal } from '../common/layout/layout-normal/layout-normal.component';
import { IndexComponent } from './index/index.component';
import { ShowComponent } from './show/show.component';
import { EditComponent } from './edit/edit.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutNormal,
    children: [
      {path: "index", component: IndexComponent},
      {path: "show", component: ShowComponent},
      {path: "edit", component: EditComponent},
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
    ShowComponent,
    EditComponent
  ],
  providers: [
    MainService,
  ]
})
export class MainModule { }

