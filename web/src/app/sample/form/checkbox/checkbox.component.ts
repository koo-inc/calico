import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable } from "rxjs/Observable";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-checkbox',
  templateUrl: './checkbox.component.html',
  styles: [`
    .large {
      width: 350px;
      height: 150px;
    }
  `]
})
export class CheckboxComponent extends DefaultFormComponent {
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
      val2: [true],
      val3: [false],
      val4: [null, Validators.required],
      val5: ['YES'],
    }));
  }

}
