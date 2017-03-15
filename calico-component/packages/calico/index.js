var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import './extension';
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CalendarModule, } from 'primeng/primeng';
import { ErrorTipComponent } from "./form/errortip.component";
import { TextFieldComponent } from "./form/textfield.component";
import { PasswordComponent } from "./form/password.component";
import { SelectComponent } from "./form/select.component";
import { RadiosComponent } from "./form/radios.component";
import { CheckboxComponent } from "./form/checkbox.component";
import { CheckboxesComponent } from "./form/checkboxes.component";
import { DatepickerComponent } from "./form/datepicker.component";
import { TimepickerComponent } from "./form/timepicker.component";
import { InfoTableDirective, ListTableDirective, } from './bootstrap/table.directive';
import { ButtonDirective } from "./bootstrap/button.directive";
import { GlyphiconDirective } from "./bootstrap/glyphicon.directive";
import { PanelComponent } from "./bootstrap/panel.component";
import { ColsComponent } from "./bootstrap/cols.component";
import { LocalStorageService } from "./util/local-storage.service";
import { SessionStorageService } from "./util/session-storage.service";
import { SerializeService } from "./util/serialize.service";
import { Api } from "./util/api.service";
import { SortComponent } from "./search/sort.component";
import { PagerComponent } from "./search/pager.component";
export { LocalStorageService } from "./util/local-storage.service";
export { SessionStorageService } from "./util/session-storage.service";
export { SerializeService } from "./util/serialize.service";
export { Api } from "./util/api.service";
export { GrowlService } from "./util/growl.service";
export { SearchService, SearchContext } from "./search/search.service";
var CalicoModule = (function () {
    function CalicoModule() {
    }
    return CalicoModule;
}());
CalicoModule = __decorate([
    NgModule({
        imports: [
            CommonModule,
            FormsModule,
            ReactiveFormsModule,
            CalendarModule,
        ],
        declarations: [
            ErrorTipComponent,
            TextFieldComponent,
            PasswordComponent,
            SelectComponent,
            RadiosComponent,
            CheckboxComponent,
            CheckboxesComponent,
            DatepickerComponent,
            TimepickerComponent,
            InfoTableDirective,
            ListTableDirective,
            ButtonDirective,
            GlyphiconDirective,
            PanelComponent,
            ColsComponent,
            SortComponent,
            PagerComponent,
        ],
        exports: [
            CommonModule,
            FormsModule,
            ReactiveFormsModule,
            ErrorTipComponent,
            TextFieldComponent,
            PasswordComponent,
            SelectComponent,
            RadiosComponent,
            CheckboxComponent,
            CheckboxesComponent,
            DatepickerComponent,
            TimepickerComponent,
            InfoTableDirective,
            ListTableDirective,
            ButtonDirective,
            GlyphiconDirective,
            PanelComponent,
            ColsComponent,
            SortComponent,
            PagerComponent,
        ],
        providers: [
            LocalStorageService,
            SessionStorageService,
            SerializeService,
            Api
        ]
    })
], CalicoModule);
export { CalicoModule };
//# sourceMappingURL=index.js.map