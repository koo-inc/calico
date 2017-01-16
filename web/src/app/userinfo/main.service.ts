import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import {Api} from "calico";

@Injectable()
export class MainService {

  constructor(
    private api: Api,
    private fb: FormBuilder,
  ) { }

  private submit<T>(url: string, form: any): Observable<T> {
    return this.api.submit('endpoint/userinfo/' + url, form);
  }

  getRecords(): Observable<Record[]>{
    return this.submit('records', {});
  }

  getRecord(id: any): Observable<Record>{
    return this.submit('record', {id: id});
  }

  getCreateForm(): Observable<FormGroup>{
    return this.submit('create_form', {})
      .map(form => this.createForm(form));
  }

  getUpdateForm(id: any): Observable<FormGroup>{
    return this.submit('update_form', {id: id})
      .map(form => this.createForm(form));
  }

  private createForm(form: any): FormGroup {
    return this.fb.group({
      id: [form.id],
      loginId: [form.loginId, Validators.required],
      password: [form.password, Validators.required],
    });
  }

  create(form: FormGroup): Observable<Record> {
    return this.submit('create', form);
  }

  update(form: FormGroup): Observable<Record> {
    return this.submit('update', form);
  }

  delete(record: Record): Observable<Record> {
    return this.submit('delete', {id: record.id});
  }

}

export class Record {
  id: number;
  loginId: string;
  password: string;
}
