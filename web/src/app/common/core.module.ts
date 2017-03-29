import { NgModule, ModuleWithProviders, Optional, SkipSelf } from '@angular/core';
import { ModalModule, PopoverModule, DropdownModule, DatepickerModule, TimepickerModule } from 'ng2-bootstrap';

import { CalicoCoreModule, CalicoUiModule, CalicoFormModule, CalicoSearchModule, MESSAGE_CONFIG } from "calico";
import { AppConfig } from "app/app.config";
import { AuthService } from "app/common/api/auth.service";
import { REQUEST_HOOK } from "calico/core/api.service";
import { VersionCheckHook, VERSION_INFO } from "./versioning/versioncheck.hook";

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
    {provide: 'LocalStorageServiceConfig', useValue: {prefix: AppConfig.appName + '-', version: AppConfig.version}},
    {provide: 'SessionStorageServiceConfig', useValue: {prefix: AppConfig.appName + '-', version: AppConfig.version}},
    {provide: 'ExtEnumServiceConfig', useValue: {apiPath: 'endpoint/system/ext_enum'}},
    {provide: 'AlertConfig', useValue: {
      common: {position: 'top-right', lifetime: 3000},
      warning: {position: 'top-right', lifetime: null},
      danger: {position: 'top-left', lifetime: null},
    }},
    {provide: MESSAGE_CONFIG, useValue: AppConfig.messages},
    {provide: VERSION_INFO, useValue: {key: AppConfig.versionTag, currentVersion: AppConfig.version}},
    VersionCheckHook,
    {provide: REQUEST_HOOK, useExisting: VersionCheckHook, multi: true}
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
