import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";

import { MainService, Record } from "../main.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
})
export class IndexComponent implements OnInit {

  constructor(
    private mainService: MainService,
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  records: Record[];

  ngOnInit() {
    this.mainService.getRecords().subscribe(records => {
      this.records = records;
    });
  }

  onSelect(record: Record): void {
    this.router.navigate(['../show', {id: record.id}], {relativeTo: this.route});
  }

}
