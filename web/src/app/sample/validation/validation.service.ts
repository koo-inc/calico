import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import {Api} from "calico";
import { AlertService } from "calico";
import { AppConfig } from "../../app.config";

@Injectable()
export class ValidationService {

  constructor(
    private api: Api,
    private alert: AlertService,
    private fb: FormBuilder,
  ) { }

  private submit<T>(url: string, form: FormGroup): Observable<T> {
    return this.api.submit('endpoint/sample/form/' + url, form.value).catch((e: any, caught: Observable<any>): Observable<any> => {
      let errors: any;
      try {
        errors = e.json();
      }
      catch (e) {
        console.error(e);
        this.alert.warning(AppConfig.messages.internalServerError);
        return;
      }
      Object.keys(errors).forEach(key => {
        let violation = errors[key].reduce((a:any, b:string) => {a[b] = true; return a}, {});
        let ctrl = form.get(key);
        if (ctrl != null) {
          ctrl.setErrors(violation);
        }
        else {
          let message = errors[key].map((msg: string) => AppConfig.messages[msg] || msg ).join('\n');
          this.alert.warning(message);
        }
      });
      throw caught;
    });
  }

  validationObject(form: FormGroup): Observable<Record> {
    return this.submit('validation_object', form).catch((e: any, caught: Observable<any>) => {console.log('catch', e, caught); return Observable.never()});
  }

  validationArray(form: FormGroup): Observable<Record> {
    return this.submit('validation_array', form).catch((e: any, caught: Observable<any>) => {console.log('catch', e, caught); return Observable.never()});
  }

  validationMap(form: FormGroup): Observable<Record> {
    return this.submit('validation_map', form).catch((e: any, caught: Observable<any>) => {console.log('catch', e, caught); return Observable.never()});
  }
}

export class Record {
  id: number;
  name: string;
}
