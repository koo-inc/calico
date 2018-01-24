import { Component } from "@angular/core";
import { AbstractControl, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable } from "rxjs/Observable";
import { of } from "rxjs/observable/of";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class ContainerComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
  ) {
    super(alert, extEnumService);
  }

  createForm(): Observable<FormGroup> {
    return of(this.fb.group({
      val1: this.fb.array([], Validators.required),
      val2: new FormGroup({}, (ctrl: AbstractControl) =>
        (ctrl.value == null || Object.getOwnPropertyNames(ctrl.value).length === 0) ? {required: true} : null
      ),
      val3: new FormGroup({
        i1: this.fb.control(null, Validators.required)
      }, (ctrl) => ({'常にエラーです': true}))
    }));
  }

}
