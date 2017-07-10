import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";

import { MainService, Record } from "../main.service";
import { SearchContext, ExtEnumService } from "calico";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  providers: [
    SearchContext
  ]
})
export class IndexComponent implements OnInit, OnDestroy {

  constructor(
    private mainService: MainService,
    private route: ActivatedRoute,
    private router: Router,
    private searchContext: SearchContext,
  ) { }

  ngOnInit() {
    this.searchContext.init({
      search: () => { return this.mainService.search(this.searchContext.form); },
      getForm: () => { return this.mainService.getSearchForm(); },
      toForm: (form: any) => { return this.mainService.toSearchForm(form); },
      initialSearch: true,
    });
  }
  ngOnDestroy(): void {
    this.searchContext.onDestroy();
  }

  onSelect(record: Record): void {
    this.router.navigate(['../show', {id: record.id}], {relativeTo: this.route});
  }

}
