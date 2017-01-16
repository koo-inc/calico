import { Observable } from "rxjs";
import 'rxjs/add/operator/map'

import { Injectable, Inject, OpaqueToken } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Http, Headers, RequestOptions } from "@angular/http";

import { GrowlService } from "./growl.service";

export class MessageConfig {
  [id: string]: string
}

export const MESSAGE_CONFIG = new OpaqueToken('MessageConfig');

@Injectable()
export class Api {
  constructor(private http: Http, private growl: GrowlService, @Inject(MESSAGE_CONFIG) private messages: MessageConfig) { }

  submit<T> (url: string): Observable<T>;
  submit<T> (url: string, form: FormGroup): Observable<T>;
  submit<T> (url: string, form: any): Observable<T>;
  submit<T> (url: string, form?: any): Observable<T> {
    let body = JSON.stringify(form instanceof FormGroup ? form.value : form);
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    let options = new RequestOptions({headers: headers});

    let req = this.http.post(url, body, options)
      .map((req, _) => req.json() as T);

    if (form instanceof FormGroup) {
      return req.catch((e: any, caught: Observable<any>): Observable<any> => {
        let errors: any;
        try {
          errors = e.json();
        }
        catch (e) {
          console.error(e);
          this.growl.error(this.messages['internalServerError'] || '500 Internal Server Error');
          throw e;
        }
        Object.keys(errors).forEach(key => {
          let violation = errors[key].reduce((a:any, b:string) => {a[b] = true; return a}, {});
          let ctrl = form.get(key);
          if (ctrl != null) {
            ctrl.setErrors(violation);
          }
          else {
            let message = errors[key].map((msg: string) => this.messages[msg] || msg ).join('\n');
            this.growl.error(message);
          }
        });
        throw e;
      });
    }
    else {
      return req.catch((e: any, caught: Observable<any>): Observable<any> => {
        let errors: any;
        try {
          e.json();
        }
        catch (e) {
          console.error(e);
          this.growl.error(this.messages['internalServerError'] || '500 Internal Server Error');
        }
        throw e;
      });
    }
  }
}
