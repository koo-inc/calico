import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from "@angular/forms";

import { MainService } from "../main.service";
import { AlertService } from "calico";

@Component({
  selector: 'app-edit-modal',
  templateUrl: './edit-modal.component.html',
})
export class EditModalComponent implements OnInit {

  constructor(
    private mainService: MainService,
    private alert: AlertService,
  ) { }

  @Input() id: number;
  @Output() onSaved: EventEmitter<number> = new EventEmitter();

  form: FormGroup;

  ngOnInit() {
    this.mainService.getEditForm(this.id).subscribe(form => {
      this.form = form;
    });
  }

  save(): void {
    this.mainService.save(this.form).subscribe(data => {
      this.alert.success('保存しました。');
      this.onSaved.emit(data.id);
    });
  }

}
