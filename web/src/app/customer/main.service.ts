import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { FormGroup, FormBuilder, Validators, FormArray } from "@angular/forms";
import { Api, SearchFormBuilder } from "calico";

@Injectable()
export class MainService {

  constructor(
    private api: Api,
    private fb: FormBuilder,
    private sfb: SearchFormBuilder,
  ) { }

  private submit<T>(url: string, data: any): Observable<T> {
    return this.api.submit('endpoint/customer/' + url, data);
  }

  getSearchForm(): Observable<FormGroup>{
    return this.submit('search_form', {})
      .map(data => this.toSearchForm(data));
  }

  toSearchForm(data: any): FormGroup {
    return this.sfb.rootGroup(data, {
      name: [data.name],
      sex: [data.sex],
    });
  }

  search(form: FormGroup): Observable<SearchResult>{
    return this.submit('search', form);
  }

  getRecord(id: any): Observable<Record>{
    return this.submit('record', {id: id});
  }

  getEditForm(id: number): Observable<FormGroup>{
    return id == null ? this.getCreateForm() : this.getUpdateForm(id);
  }

  private getCreateForm(): Observable<FormGroup>{
    return this.submit('create_form', {})
      .map(data => this.toEditForm(data));
  }

  private getUpdateForm(id: number): Observable<FormGroup>{
    return this.submit('update_form', {id: id})
      .map(data => this.toEditForm(data));
  }

  private toEditForm(data: any): FormGroup {
    return this.fb.group({
      id: [data.id],
      kname1: [data.kname1, Validators.required],
      kname2: [data.kname2, Validators.required],
      fname1: [data.fname1],
      fname2: [data.fname1],
      sex: [data.sex],
      favoriteNumber: [data.favoriteNumber],
      claimer: [data.claimer],
      birthday: [data.birthday],
      contactEnableStartTime: [data.contactEnableStartTime],
      contactEnableEndTime: [data.contactEnableEndTime],
      email: [data.email],
      homepageUrl: [data.homepageUrl],
      phoneNumber: [data.phoneNumber],
      // photo: [data.photo],
      families: this.fb.array(
        (<any[]>data.families).map((family: any) => this.toEditFamilyForm(family))
      ),
      // additionalInfoList: [],
    });
  }

  private toEditFamilyForm(family: any): FormGroup {
    return this.fb.group({
      id: [family.id],
      familyType: [family.familyType],
      name: [family.name, Validators.required],
      sex: [family.sex],
      favoriteNumber: [family.favoriteNumber],
      birthday: [family.birthday],
    })
  }

  addFamily(form: FormGroup): void {
    let families: FormArray = form.get('families') as FormArray;
    families.push(this.toEditFamilyForm({}));
  }

  removeFamily(form: FormGroup, index: number): void {
    let families: FormArray = form.get('families') as FormArray;
    families.removeAt(index);
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

export interface SearchResult {
  _count: number;
  records: {
    id: number;
    kname1: string;
    kname2: string;
    sex: number;
    birthday: string;
  }[];
}

export class Record {
  id: number;
  kname1: string;
  kname2: string;
  fname1: string;
  fname2: string;
  sex: number;
  birthday: string;
}
