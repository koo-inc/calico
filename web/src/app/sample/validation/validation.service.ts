import { Injectable } from '@angular/core';
import { Observable ,  NEVER as never } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import {Api} from "calico";
import { AlertService } from "calico";

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
    return this.submit('validation_object', form).pipe(catchError((e: any, caught: Observable<any>) => {
      console.log('catch', e, caught);
      return never;
    }));
  }

  validationArray(form: FormGroup): Observable<Record> {
    return this.submit('validation_array', form).pipe(catchError((e: any, caught: Observable<any>) => {
      console.log('catch', e, caught);
      return never;
    }));
  }

  validationMap(form: FormGroup): Observable<Record> {
    return this.submit('validation_map', form).pipe(catchError((e: any, caught: Observable<any>) => {
      console.log('catch', e, caught);
      return never;
    }));
  }
}

export class Record {
  id: number;
  name: string;
}
