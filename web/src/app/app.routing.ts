import { Type } from "@angular/core";
import { Route, RouterModule, Routes } from '@angular/router';
import { EnsureRemoteData } from 'calico';

import { SessionSupport } from "./common/api/auth.service";
import { LayoutNormal } from "app/common/layout/layout-normal/layout-normal.component";

let routes: Route[] = [
  { path: '', redirectTo: '/top', pathMatch: 'full' },
  { path: 'login', loadChildren: 'app/login/login.module#LoginModule' },
  { path: 'top', loadChildren: 'app/top/top.module#TopModule' },
  { path: 'userinfo', loadChildren: 'app/userinfo/main.module#MainModule' },
  { path: 'customer', loadChildren: 'app/customer/main.module#MainModule' },
  { path: 'sample', loadChildren: 'app/sample/main.module#MainModule' },
];
export const routing = RouterModule.forRoot(routes);

export function buildRoute(params: {layout?: Type<any>, children: Routes, canActivateChild?: any[]}): Route {
  return {
    path: '',
    component: params.layout || LayoutNormal,
    children: params.children,
    canActivateChild: params.canActivateChild || [SessionSupport, EnsureRemoteData]
  };
}

