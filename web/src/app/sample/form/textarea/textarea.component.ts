import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable } from "rxjs/Observable";
import { of } from "rxjs/observable/of";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-textarea',
  templateUrl: './textarea.component.html',
  styles: [`
    .large {
      width: 350px;
      height: 150px;
    }
  `]
})
export class TextareaComponent extends DefaultFormComponent {
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
      val2: ['あいうえお\nかきくけこ'],
      val3: [null],
      val4: [null, Validators.required],
    }));
  }

}
