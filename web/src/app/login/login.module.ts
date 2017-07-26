import { NgModule } from '@angular/core';
import { Routes, RouterModule } from "@angular/router";

import { SharedModule } from "app/common/shared.module";
import { LayoutLogin } from 'app/common/layout/layout-login/layout-login.component';

import { LoginComponent } from "./login.component";
import { buildRoute } from "app/app.routing";
import { SessionSupport } from "app/common/api/auth.service";


const routes: Routes = [
  buildRoute({
    layout: LayoutLogin,
    children: [
      {path: "", component: LoginComponent},
    ],
    canActivateChild: [SessionSupport]
  }),
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
