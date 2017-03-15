import { NgModule, ModuleWithProviders, Optional, SkipSelf } from '@angular/core';

import { MESSAGE_CONFIG } from "calico/util/api.service";
import { SearchService } from "calico";

import { AlertModule, ModalModule, PopoverModule, DatepickerModule, TimepickerModule } from 'ng2-bootstrap';

import { AuthService } from "app/common/api/auth.service";
import { ExtEnumService } from "app/common/ext-enum/ext-enum.service";
import { AppConfig } from "app/app.config";
import { AlertService } from "calico";

@NgModule({
  imports: [
    ModalModule.forRoot(),
    PopoverModule.forRoot(),
    AlertModule.forRoot(),
    DatepickerModule.forRoot(),
    TimepickerModule.forRoot(),
  ],
  exports: [
  ],
  providers: [
    AuthService,
    ExtEnumService,
    AlertService,
    SearchService,
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
      throw new Error(
        'CoreModule is already loaded. Import it in the AppModule only');
    }
  }
}
