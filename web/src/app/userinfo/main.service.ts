import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Api } from "calico";
import { map } from "rxjs/operators";

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

  getEditForm(id: number): Observable<FormGroup>{
    return id == null ? this.getCreateForm() : this.getUpdateForm(id);
  }

  private getCreateForm(): Observable<FormGroup>{
    return this.submit('create_form', {})
      .pipe(map(data => this.toEditForm(data)));
  }

  private getUpdateForm(id: number): Observable<FormGroup>{
    return this.submit('update_form', {id: id})
      .pipe(map(data => this.toEditForm(data)));
  }

  private toEditForm(data: any): FormGroup {
    return this.fb.group({
      id: [data.id],
      loginId: [data.loginId, Validators.required],
      password: [data.password, Validators.required],
    });
  }

  save(form: FormGroup): Observable<Record> {
    return form.value.id == null ? this.create(form) : this.update(form);
  }

  private create(form: FormGroup): Observable<Record> {
    return this.submit('create', form);
  }

  private update(form: FormGroup): Observable<Record> {
    return this.submit('update', form);
  }

  delete(id: number): Observable<Record> {
    return this.submit('delete', {id: id});
  }

}

export class Record {
  id: number;
  loginId: string;
  password: string;
}
