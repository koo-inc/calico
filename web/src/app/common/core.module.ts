import { NgModule, ModuleWithProviders, Optional, SkipSelf } from '@angular/core';
import { ModalModule, PopoverModule, DropdownModule, DatepickerModule, TimepickerModule } from 'ng2-bootstrap';

import { CalicoCoreModule, CalicoUiModule, CalicoFormModule, CalicoSearchModule, MESSAGE_CONFIG } from "calico";
import { AppConfig } from "app/app.config";
import { AuthService } from "app/common/api/auth.service";

@NgModule({
  imports: [
    ModalModule.forRoot(),
    PopoverModule.forRoot(),
    DropdownModule.forRoot(),
    DatepickerModule.forRoot(),
    TimepickerModule.forRoot(),
    CalicoCoreModule.forRoot(),
    CalicoUiModule.forRoot(),
    CalicoFormModule.forRoot(),
    CalicoSearchModule.forRoot(),
  ],
  exports: [
  ],
  providers: [
    AuthService,
    {provide: 'LocalStorageServiceConfig', useValue: {prefix: AppConfig.appName + '-', deployedAt: AppConfig.deployedAt}},
    {provide: 'SessionStorageServiceConfig', useValue: {prefix: AppConfig.appName + '-', deployedAt: AppConfig.deployedAt}},
    {provide: 'ExtEnumServiceConfig', useValue: {apiPath: 'endpoint/system/ext_enum'}},
    {provide: 'AlertConfig', useValue: {
      common: {position: 'top-right', lifetime: 3000},
      warning: {position: 'top-left', lifetime: null},
      danger: {position: 'top-left', lifetime: null},
    }},
    {provide: MESSAGE_CONFIG, useValue: AppConfig.messages}
  ]
})
export class CoreModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: CoreModule
    };
  }
  constructor (@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule is already loaded. Import it in the AppModule only');
    }
  }
}
