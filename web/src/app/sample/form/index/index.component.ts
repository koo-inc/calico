import { Component, Host, Input, OnInit, Optional, SkipSelf, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
import { ControlContainer, FormGroup, FormGroupDirective } from "@angular/forms";
import { Observable } from "rxjs";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-index',
  template: `
    <tabs></tabs>
  `,
})
export class FormIndexComponent {}

@Component({
  selector: 'tabs',
  template: `
    <ul class="nav nav-tabs" role="tablist">
      <li *ngFor="let tab of tabs"
        [class.active]="isActive(tab)">
        <a (click)="select(tab)">{{tab.name}}</a>
      </li>
    </ul>
  `,
})
export class FormTabsComponent {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  tabs: any[] = [
    { name: 'c-textfield', path: 'textfield', },
    { name: 'c-password', path: 'password', },
    { name: 'c-textarea', path: 'textarea', },
    { name: 'c-select', path: 'select', },
    { name: 'c-radios', path: 'radios', },
    { name: 'c-checkboxes', path: 'checkboxes', },
    { name: 'c-checkbox', path: 'checkbox', },
    { name: 'c-datepicker', path: 'datepicker', },
    { name: 'c-timepicker', path: 'timepicker', },
    { name: 'c-integer', path: 'integer', },
    { name: 'c-currency', path: 'currency', },
  ];

  select(tab: any): void {
    this.router.navigate(['/sample/form/' + tab.path]);
  }

  isActive(tab: any): boolean {
    return tab.path == this.route.snapshot.url.last().path;
  }
}

@Component({
  selector: '[inspect]',
  template: `
    {{value | json}}
  `,
  styles: [`
    :host {
      display: block;
      margin: 0.5em 0;
      padding: 0.3em;
      border: 1px solid #ccc;
      border-radius: 4px;
      background-color: #f3fff3;
    }
  `]
})
export class FormInspectComponent {
  constructor(
    @Optional() @Host() @SkipSelf() private parent: ControlContainer
  ) {}

  @Input('inspect') prop: string;

  get value(): any {
    if(this.parent.formDirective instanceof FormGroupDirective){
      return this.parent.formDirective.form.get(this.prop).value;
    }else{
      throw new Error('cannot get FormGroup');
    }
  }
}

export abstract class DefaultFormComponent implements OnInit {
  constructor(
    public alert: AlertService,
    public extEnumService: ExtEnumService,
  ) {}

  form: FormGroup;
  @ViewChild(FormGroupDirective) formGroupDirective: FormGroupDirective;

  options = {};

  ngOnInit(): void {
    this.extEnumService.setValues(this.options, 'sex', 'familyType');
    this.createForm().subscribe(form => {
      this.form = form;
    });
  }

  abstract createForm(): Observable<FormGroup>

  submit(): void {
    if(this.form.invalid) {
      this.alert.warning('invalid', {lifetime: 3000});
      return;
    }else{
      this.createForm().subscribe(form => {
        this.form = form;
      });
      this.formGroupDirective.resetForm();
      this.alert.success('submitted');
    }
  }
}
