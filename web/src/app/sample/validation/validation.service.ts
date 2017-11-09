import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

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
    return this.api.submit('endpoint/sample/form/' + url, form);
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
