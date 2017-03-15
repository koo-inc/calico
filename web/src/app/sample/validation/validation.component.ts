import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormGroup, FormControl, Validators, FormArray } from "@angular/forms";

import { ValidationService } from "./validation.service";
import { AlertService } from "calico";

@Component({
  selector: 'app-validation',
  templateUrl: './validation.component.html',
})
export class ValidationComponent implements OnInit {

  constructor(
    private service: ValidationService,
    private route: ActivatedRoute,
    private router: Router,
    private alert: AlertService,
  ) { }

  id: number;
  form1: FormGroup;
  form2: FormGroup;
  form3: FormGroup;

  ngOnInit() {
    this.form1 = new FormGroup({
      id: new FormControl(null),
      name: new FormControl(null),
    });

    this.form2 = new FormGroup({
      id: new FormControl(null),
      name: new FormControl(null),
      children: new FormArray([
        new FormGroup({
          id: new FormControl(null),
          name: new FormControl(null),
        })
      ])
    });

    this.form3 = new FormGroup({
      id: new FormControl(null),
      name: new FormControl(null),
      childMap: new FormGroup({})
    });
  }

  getForm3ChildMapKey() {
    return Object.keys((this.form3.get('childMap') as FormGroup).controls);
  }

  addNewChild() {
    (this.form2.get('children') as FormArray).push(
      new FormGroup({
        id: new FormControl(null),
        name: new FormControl(null),
      })
    );
  }

  count = 0;
  putNewChild() {
    (this.form3.get('childMap') as FormGroup).addControl('key-' + (this.count++), new FormGroup({
      id: new FormControl(null),
      name: new FormControl(null),
    }));
  }

  save1() {
    this.service.validationObject(this.form1).subscribe();
  }

  save2() {
    this.service.validationArray(this.form2).subscribe();
  }

  save3() {
    this.service.validationMap(this.form3).subscribe();
  }
}
