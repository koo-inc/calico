import { Route, RouterModule } from '@angular/router';
import { SessionSupport } from "./common/api/auth.service";

let routes: Route[] = [
  { path: '', redirectTo: '/top', pathMatch: 'full' },
  { path: 'login', loadChildren: 'app/login/login.module#LoginModule' },
  { path: 'top', loadChildren: 'app/top/top.module#TopModule' },
  { path: 'userinfo', loadChildren: 'app/userinfo/main.module#MainModule' },
  { path: 'customer', loadChildren: 'app/customer/main.module#MainModule' },
  { path: 'sample', loadChildren: 'app/sample/main.module#MainModule' },
];

routes = addCanActivate(routes, SessionSupport);

export const routing = RouterModule.forRoot(routes);

function addCanActivate(routes: Route[], canActivate: any) {
  routes.forEach(route => {
    if (route.canActivate == null) {
      route.canActivate = [];
    }
    route.canActivate.push(canActivate);
  });
  return routes;
}
