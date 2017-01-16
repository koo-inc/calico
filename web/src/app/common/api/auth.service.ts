import { Injectable } from '@angular/core';
import { FormGroup, Validators, FormControl } from "@angular/forms";
import { Observable, Subject } from "rxjs";

import { Api } from "calico/util/api.service";

export interface AuthInfo {
  userId: number;
  loginId: string;
  authenticated: boolean;
}

const _authState: Subject<AuthInfo> = new Subject();
export const authState: Observable<AuthInfo> = _authState;

@Injectable()
export class AuthService {
  constructor(private api: Api) { }

  login(form: FormGroup) {
    return this.api.submit("endpoint/auth/login", form)
      .do((authInfo: AuthInfo) => _authState.next(authInfo));
  }
  logout() {
    return this.api.submit("endpoint/auth/logout")
      .do((authInfo: AuthInfo) => _authState.next(authInfo));
  }
  keep(): Observable<AuthInfo> {
    return this.api.submit("endpoint/auth/keep")
      .do((authInfo: AuthInfo) => _authState.next(authInfo));
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
