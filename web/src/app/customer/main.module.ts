import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SharedModule } from "../common/shared.module";
import { MainService } from "./main.service";
import { IndexComponent } from './index/index.component';
import { ShowComponent } from './show/show.component';
import { EditComponent } from './edit/edit.component';
import { buildRoute } from "app/app.routing";

const routes: Routes = [
  buildRoute({
    children: [
      {path: "index", component: IndexComponent},
      {path: "show", component: ShowComponent},
      {path: "edit", component: EditComponent},
    ],
  }),
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

