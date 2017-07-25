import { Route, RouterModule } from '@angular/router';

let routes: Route[] = [
  { path: '', redirectTo: '/top', pathMatch: 'full' },
  { path: 'login', loadChildren: 'app/login/login.module#LoginModule' },
  { path: 'top', loadChildren: 'app/top/top.module#TopModule' },
  { path: 'userinfo', loadChildren: 'app/userinfo/main.module#MainModule' },
  { path: 'customer', loadChildren: 'app/customer/main.module#MainModule' },
  { path: 'sample', loadChildren: 'app/sample/main.module#MainModule' },
];
export const routing = RouterModule.forRoot(routes);
