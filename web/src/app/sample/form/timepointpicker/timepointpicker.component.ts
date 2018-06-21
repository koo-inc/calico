import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable ,  of } from "rxjs";
import { AlertService, ExtEnumService } from "calico";
import { Api } from "calico/core/api.service";
import { TimePoint } from "calico/type/time";

@Component({
  selector: 'app-timepointpicker',
  templateUrl: './timepointpicker.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class TimePointPickerComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
    private api: Api,
  ) {
    super(alert, extEnumService);
  }

  max = TimePoint.create('11:03');
  min = TimePoint.create('09:31');

  createForm(): Observable<FormGroup> {
    this.api.submit("endpoint/sample/form/time_point_value", {value: TimePoint.create('27:15')}).subscribe((data: any) => {
      this.form.patchValue({val6: TimePoint.create(data.value)});
    });
    return of(this.fb.group({
      val1: [null],
      val2: [TimePoint.create(100)],
      val3: [TimePoint.create(12, 0)],
      val4: [TimePoint.create('10:00'), Validators.required],
      val5: [TimePoint.create('10:00')],
      val6: [null],
    }));
  }
}
