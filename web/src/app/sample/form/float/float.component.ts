import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable ,  of } from "rxjs";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-float',
  templateUrl: './float.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class FloatComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
  ) {
    super(alert, extEnumService);
  }

  createForm(): Observable<FormGroup> {
    return of(this.fb.group({
      val1: [null],
      val2: [12345],
      val3: [null],
      val4: [null, Validators.required],
      val5: [-12345],
      val6: [null],
    }));
  }

}
