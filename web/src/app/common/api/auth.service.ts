import { Injectable } from '@angular/core';
import { FormGroup, Validators, FormControl } from "@angular/forms";
import { Observable } from "rxjs/Observable";
import { map, tap } from "rxjs/operators";

import { Api } from "calico";
import { ActivatedRouteSnapshot, CanActivateChild, Router, RouterStateSnapshot } from "@angular/router";

export interface AuthInfo {
  userId: number;
  loginId: string;
  authenticated: boolean;
}

@Injectable()
export class AuthService {
  constructor(private api: Api) { }

  private _authInfo: AuthInfo;

  get authInfo() {
    if (this._authInfo == null) throw new Error("権限情報が取得されていません");
    return this._authInfo;
  }

  login(form: FormGroup) {
    return this.submit("login", form);
  }
  logout() {
    return this.submit("logout");
  }
  keep(): Observable<AuthInfo> {
    return this.submit("keep");
  }

  getForm(): Promise<FormGroup> {
    return new Promise<FormGroup>((resolve, reject) => {
      resolve(loginForm('', ''));
    });
  }

  private submit(url: string, form?: any) {
    return tap((authInfo: AuthInfo) => this._authInfo = authInfo)
      (this.api.submit(`endpoint/auth/${url}`, form));
  }
}

export interface LoginForm {
  loginId: string;
  password: string;
}
export const loginForm = (loginId: string, password: string): FormGroup => {
  return new FormGroup({
    loginId: new FormControl(loginId, Validators.required),
    password: new FormControl(password, Validators.required)
  });
};

@Injectable()
export class SessionSupport implements CanActivateChild {
  constructor(private authService: AuthService, private router: Router) {
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.authService.keep().pipe(map(authInfo => this.checkAuthenticatedIfNeeded(authInfo, state)));
  }

  private checkAuthenticatedIfNeeded(authInfo: AuthInfo, state: RouterStateSnapshot) {
    if (state.url == '/login') {
      return true;
    }
    if (!authInfo.authenticated) {
      this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}
