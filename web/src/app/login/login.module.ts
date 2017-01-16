import { NgModule } from '@angular/core';
import { Routes, RouterModule } from "@angular/router";

import { SharedModule } from "app/common/shared.module";
import { LayoutLogin } from 'app/common/layout/layout-login/layout-login.component';

import { LoginComponent } from "./login.component";


const routes: Routes = [
  {
    path: '',
    component: LayoutLogin,
    children: [
      {path: "", component: LoginComponent},
    ],
  },
];

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
  ],
  declarations: [
    LoginComponent,
  ],
})
export class LoginModule { }
