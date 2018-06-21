import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable ,  of } from "rxjs";
import { AlertService, ExtEnumService, ExtEnum } from "calico";

@Component({
  selector: 'app-checkboxes',
  templateUrl: './checkboxes.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class CheckboxesComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
  ) {
    super(alert, extEnumService);
  }

  private observable$: Observable<ExtEnum[]>;

  ngOnInit(): void {
    super.ngOnInit();

    this.observable$ = of(
      this.extEnumService.getValues('Sex')
    );
  }

  createForm(): Observable<FormGroup> {
    let data = this.extEnumService.getAll();
    return of(
      this.fb.group({
        val1: [null],
        val2: [[data['Sex'][0]]],
        val3: [null, Validators.required],
        val4: [[data['FamilyType'][0]]],
        val5: [null],
        val6: [[data['FamilyType'][0]['NAME'], data['FamilyType'][3]['NAME']]],
        val7: [[2,5]],
        val8: [[data['Sex'][0]]],
        val9: [[data['FamilyType'][1], data['FamilyType'][3]]],
        val10: [[data['FamilyType'][0], data['FamilyType'][2]]],
        val11: [[data['FamilyType'][0], data['FamilyType'][1]]],
      })
    );
  }

}
