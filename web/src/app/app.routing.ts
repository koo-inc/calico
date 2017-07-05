import { Route, RouterModule } from '@angular/router';
import { EnsureRemoteData } from 'calico';

export function addRemoteDataSupport(route: Route): Route {
  if(route.loadChildren){
    route.canActivateChild = [EnsureRemoteData];
  }
  return route;
}

const routes = [
  { path: '', redirectTo: '/top', pathMatch: 'full' },
  { path: 'login', loadChildren: 'app/login/login.module#LoginModule' },
  { path: 'top', loadChildren: 'app/top/top.module#TopModule' },
  { path: 'userinfo', loadChildren: 'app/userinfo/main.module#MainModule' },
  { path: 'customer', loadChildren: 'app/customer/main.module#MainModule' },
  { path: 'sample', loadChildren: 'app/sample/main.module#MainModule' },
];
export const routing = RouterModule.forRoot(routes.map((r: Route) => addRemoteDataSupport(r)));
