import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable } from "rxjs";
import { AlertService, ExtEnumService } from "calico";

@Component({
  selector: 'app-enum-select',
  templateUrl: './enum-select.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class EnumSelectComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
  ) {
    super(alert, extEnumService);
  }

  private val4Shown = false;

  ngOnInit(): void {
    localStorage.clear();
    super.ngOnInit();
    setTimeout(() => {
      this.val4Shown = true;
    }, 1000);
  }

  createForm(): Observable<FormGroup> {
    return Observable.of(
      this.fb.group({
        val1: [null],
        val2: [{id: 1}],
        val3: [null, Validators.required],
        val4: [{id: 1}],
      })
    );
  }

}
