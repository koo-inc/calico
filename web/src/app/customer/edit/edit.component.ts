import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormGroup } from "@angular/forms";

import { MainService } from "../main.service";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
})
export class EditComponent implements OnInit {

  constructor(
    private mainService: MainService,
    private route: ActivatedRoute,
    private router: Router,
    private alert: AlertService,
    private extEnumService: ExtEnumService,
  ) { }

  id: number;
  form: FormGroup;
  options = {};

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'] == null ? null : +params['id'];
      this.mainService.getEditForm(this.id).subscribe(form => {
        this.form = form;
      });
    });
    this.extEnumService.setValues(this.options, 'sex', 'familyType');
  }

  addFamily(): void {
    this.mainService.addFamily(this.form);
  }

  removeFamily(index: number): void {
    this.mainService.removeFamily(this.form, index);
  }

  save(): void {
    this.mainService.save(this.form).subscribe(data => {
      this.alert.success('保存しました。');
      this.router.navigate(['../show', {id: data.id}], {relativeTo: this.route});
    });
  }

}
