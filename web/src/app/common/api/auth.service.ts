import { Injectable } from '@angular/core';
import { FormGroup, Validators, FormControl } from "@angular/forms";
import { Observable } from "rxjs";

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
    return this.api.submit("endpoint/auth/login", form)
      .do((authInfo: AuthInfo) => this._authInfo = authInfo);
  }
  logout() {
    return this.api.submit("endpoint/auth/logout")
      .do((authInfo: AuthInfo) => this._authInfo = authInfo);
  }
  keep(): Observable<AuthInfo> {
    return this.api.submit("endpoint/auth/keep")
      .do((authInfo: AuthInfo) => this._authInfo = authInfo);
  }

  getForm(): Promise<FormGroup> {
    return new Promise<FormGroup>((resolve, reject) => {
      resolve(loginForm('', ''));
    });
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
    return this.authService.keep().map(authInfo => this.checkAuthenticatedIfNeeded(authInfo, state));
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
