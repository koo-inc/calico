import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";

import { MainService, Record } from "../main.service";
import { SearchContext, ExtEnumService, AlertService } from "calico";
import { FormGroup } from "@angular/forms";
import { download } from 'calico/util/file';
import { Media } from 'calico/type/media';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  providers: [
    SearchContext
  ]
})
export class IndexComponent implements OnInit, OnDestroy {
  private uploadForm: FormGroup;
  private errorCsv: Media;

  constructor(
    private mainService: MainService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService,
    public searchContext: SearchContext,
  ) { }

  ngOnInit() {
    this.searchContext.init({
      search: () => { return this.mainService.search(this.searchContext.form); },
      getForm: () => { return this.mainService.getSearchForm(); },
      toForm: (form: any) => { return this.mainService.toSearchForm(form); },
      initialSearch: true,
    });

    this.uploadForm = this.mainService.getUploadForm();
  }
  ngOnDestroy(): void {
    this.searchContext.onDestroy();
  }

  onSelect(record: Record): void {
    this.router.navigate(['../show', {id: record.id}], {relativeTo: this.route});
  }

  upload(): void {
    this.mainService.upload(this.uploadForm).subscribe(result => {
      if (result.error) {
        this.errorCsv = result.file;
        this.alertService.warning("データの取込に失敗しました。")
      }
      else {
        this.searchContext.search();
        this.alertService.success(`${result.count}件のデータを取り込みました。`)
      }
    });
  }

  download(): void {
    this.mainService.download(this.searchContext.form).subscribe(file => {
      download(file);
    });
  }
}
