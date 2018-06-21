import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable ,  of } from "rxjs";
import { AlertService, ExtEnumService } from "calico";
import { Api } from "calico/core/api.service";

@Component({
  selector: 'app-timepicker',
  templateUrl: './timepicker.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class TimepickerComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
    private api: Api,
  ) {
    super(alert, extEnumService);
  }

  createForm(): Observable<FormGroup> {
    this.api.submit("endpoint/sample/form/time_value").subscribe((data: any) => {
      this.form.addControl("val5", this.fb.control(data.value, []))
    });
    return of(this.fb.group({
      val1: [null],
      val2: [Date.create().toISOString()],
      val3: [null],
      val4: [null, Validators.required],
      val6: [Date.create()],
    }));
  }

  typeOf(data: any) {
    return typeof data;
  }
}
