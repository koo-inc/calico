import { Component, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable ,  of } from "rxjs";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-textfield',
  templateUrl: './textfield.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class TextfieldComponent extends DefaultFormComponent {
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
      val2: ['あいうえお'],
      val3: [null],
      val4: [null, Validators.required],
    }));
  }

  eventValue: string;
}
