import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormGroup } from "@angular/forms";

import { MainService } from "../main.service";
import { GrowlService } from "calico";
import { ExtEnum, ExtEnumService } from "app/common/ext-enum/ext-enum.service";

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
})
export class EditComponent implements OnInit {

  constructor(
    private mainService: MainService,
    private route: ActivatedRoute,
    private router: Router,
    private growlService: GrowlService,
    private extEnumService: ExtEnumService,
  ) { }

  id: number;
  form: FormGroup;

  sexes: ExtEnum[] = [];
  familyTypes: ExtEnum[] = [];

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'] == null ? null : +params['id'];
      if(this.id == null){
        this.mainService.getCreateForm().subscribe(form => {
          this.form = form;
        });
      }else{
        this.mainService.getUpdateForm(this.id).subscribe(form => {
          this.form = form;
        });
      }
    });
    this.extEnumService.values('sex', 'familyType').subscribe(data => {
      this.sexes = data['sex'];
      this.familyTypes = data['familyType'];
    });
  }

  addFamily(): void {
    this.mainService.addFamily(this.form);
  }

  removeFamily(index: number): void {
    this.mainService.removeFamily(this.form, index);
  }

  save(): void {
    if(this.form.invalid){
      return;
    }
    if(this.id == null){
      this.mainService.create(this.form).subscribe((record) => {
        this.growlService.savedMessage();
        this.router.navigate(['../show', {id: record.id}], {relativeTo: this.route});
      });
    }else{
      this.mainService.update(this.form).subscribe(() => {
        this.growlService.savedMessage();
        this.router.navigate(['../show', {id: this.id}], {relativeTo: this.route});
      });
    }
  }

}
