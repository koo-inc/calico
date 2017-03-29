import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable } from "rxjs/Observable";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styles: [`
  `]
})
export class DatepickerComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
  ) {
    super(alert, extEnumService);
  }

  createForm(): Observable<FormGroup> {
    return Observable.of(this.fb.group({
      val1: [null],
      val2: [Date.create().toISOString()],
      val3: [null],
      val4: [null, Validators.required],
    }));
  }

}
