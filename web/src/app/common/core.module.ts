import { NgModule, ModuleWithProviders, Optional, SkipSelf } from '@angular/core';
import { ModalModule, PopoverModule, BsDropdownModule, DatepickerModule, TimepickerModule } from 'ngx-bootstrap';

import { CalicoCoreModule, CalicoUiModule, CalicoFormModule, CalicoSearchModule, MESSAGE_CONFIG } from "calico";
import { AppConfig } from "app/app.config";
import { AuthService } from "app/common/api/auth.service";
import { REQUEST_HOOK } from "calico/core/api.service";
import { VersionCheckHook, VERSION_INFO } from "./versioning/versioncheck.hook";

import { ALERT_CONFIG } from "calico/ui/alert.service";
import { EXT_ENUM_SERVICE_CONFIG } from "calico/core/ext-enum.service";
import { SESSION_STORAGE_SERVICE_CONFIG } from "calico/core/session-storage.service";
import { LOCAL_STORAGE_SERVICE_CONFIG } from "calico/core/local-storage.service";


@NgModule({
  imports: [
    ModalModule.forRoot(),
    PopoverModule.forRoot(),
    BsDropdownModule.forRoot(),
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
    {provide: LOCAL_STORAGE_SERVICE_CONFIG, useValue: {prefix: AppConfig.appName + '-', version: AppConfig.version}},
    {provide: SESSION_STORAGE_SERVICE_CONFIG, useValue: {prefix: AppConfig.appName + '-', version: AppConfig.version}},
    {provide: EXT_ENUM_SERVICE_CONFIG, useValue: {apiPath: 'endpoint/system/ext_enum'}},
    {provide: ALERT_CONFIG, useValue: {
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
