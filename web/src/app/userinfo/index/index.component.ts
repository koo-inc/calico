import { Component, OnInit, ViewChild } from '@angular/core';

import { MainService, Record } from "../main.service";
import { ModalComponent } from "calico";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
})
export class IndexComponent implements OnInit {

  constructor(
    private mainService: MainService,
  ) { }

  records: Record[];

  ngOnInit() {
    this.getRecords();
  }

  private getRecords(): void {
    this.mainService.getRecords().subscribe(records => {
      this.records = records;
    });
  }

  @ViewChild('editModal')
  editModal: ModalComponent;
  editId: number = null;

  editRecord(id: number): void {
    this.editId = id;
    this.editModal.show();
  }

  onSaved(): void {
    this.editModal.hide();
    this.getRecords();
  }

}
