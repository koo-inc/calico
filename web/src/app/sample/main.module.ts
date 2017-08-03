import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EnsureRemoteData } from 'calico';

import { SharedModule } from "../common/shared.module";
import { ValidationService } from "./validation/validation.service";
import { LayoutNormal } from '../common/layout/layout-normal/layout-normal.component';
import { IndexComponent } from './index/index.component';
import { ValidationComponent } from './validation/validation.component';
import { PopoverComponent } from "./popover/popover.component";
import { FormIndexComponent, FormTabsComponent, FormInspectComponent } from "./form/index/index.component";
import { TextfieldComponent } from "./form/textfield/textfield.component";
import { PasswordComponent } from "./form/password/password.component";
import { TextareaComponent } from "./form/textarea/textarea.component";
import { SelectComponent } from "./form/select/select.component";
import { RadiosComponent } from "./form/radios/radios.component";
import { CheckboxesComponent } from "./form/checkboxes/checkboxes.component";
import { CheckboxComponent } from "./form/checkbox/checkbox.component";
import { DatepickerComponent } from "./form/datepicker/datepicker.component";
import { TimepickerComponent } from "./form/timepicker/timepicker.component";
import { IntegerComponent } from "./form/integer/integer.component";
import { CurrencyComponent } from "./form/currency/currency.component";
import { FloatComponent } from "./form/float/float.component";
import { FileComponent } from "./form/file/file.component";
import { buildRoute } from "app/app.routing";

const routes: Routes = [
  buildRoute({
    children: [
      {path: "", component: IndexComponent},
      {path: "validation", component: ValidationComponent},
      {path: "popover", component: PopoverComponent},
      {path: "form", component: FormIndexComponent},
      {path: "form/textfield", component: TextfieldComponent},
      {path: "form/password", component: PasswordComponent},
      {path: "form/textarea", component: TextareaComponent},
      {path: "form/select", component: SelectComponent},
      {path: "form/radios", component: RadiosComponent},
      {path: "form/checkboxes", component: CheckboxesComponent},
      {path: "form/checkbox", component: CheckboxComponent},
      {path: "form/datepicker", component: DatepickerComponent},
      {path: "form/timepicker", component: TimepickerComponent},
      {path: "form/integer", component: IntegerComponent},
      {path: "form/currency", component: CurrencyComponent},
      {path: "form/float", component: FloatComponent},
      {path: "form/file", component: FileComponent},
    ],
    }),
];

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
  ],
  declarations: [
    IndexComponent,
    ValidationComponent,
    PopoverComponent,
    FormIndexComponent,
    FormTabsComponent,
    FormInspectComponent,
    TextfieldComponent,
    PasswordComponent,
    TextareaComponent,
    SelectComponent,
    RadiosComponent,
    CheckboxesComponent,
    CheckboxComponent,
    DatepickerComponent,
    TimepickerComponent,
    IntegerComponent,
    CurrencyComponent,
    FloatComponent,
    FileComponent,
  ],
  providers: [
    ValidationService,
  ]
})
export class MainModule { }

