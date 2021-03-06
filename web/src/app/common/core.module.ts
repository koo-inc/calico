import { NgModule, ModuleWithProviders, Optional, SkipSelf } from '@angular/core';
import { ModalModule } from 'ngx-bootstrap/modal';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { DatepickerModule } from 'ngx-bootstrap/datepicker';
import { TimepickerModule } from 'ngx-bootstrap/timepicker';

import {
  CalicoCoreModule,
  CalicoUiModule,
  CalicoFormModule,
  CalicoSearchModule,
  MESSAGE_CONFIG,
  REMOTE_DATA_TYPE,
  EXT_ENUM_DATA_PROVIDER,
} from "calico";
import { LOCAL_STORAGE_SERVICE_CONFIG } from "calico/core/local-storage.service";
import { SESSION_STORAGE_SERVICE_CONFIG } from "calico/core/session-storage.service";
import { ALERT_CONFIG } from "calico/ui/alert.service";
import { REQUEST_HOOK } from "calico/core/api.service";

import { AppConfig } from "app/app.config";
import { AuthService, SessionSupport } from "app/common/api/auth.service";
import { AppExtEnumDataProvider, EXT_ENUM, FAMILY_TYPE, NOT_ENSURED_FAMILY_TYPE } from "app/common/remote-data.config";
import { VersionCheckHook, VERSION_INFO } from "./versioning/versioncheck.hook";

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
    SessionSupport,
    {provide: LOCAL_STORAGE_SERVICE_CONFIG, useValue: {prefix: AppConfig.appName + '-', version: AppConfig.version}},
    {provide: SESSION_STORAGE_SERVICE_CONFIG, useValue: {prefix: AppConfig.appName + '-', version: AppConfig.version}},
    {provide: ALERT_CONFIG, useValue: {
      common: {position: 'top-right', lifetime: 3000},
      warning: {position: 'top-left', lifetime: null},
      danger: {position: 'top-left', lifetime: null},
    }},
    {provide: MESSAGE_CONFIG, useValue: AppConfig.messages},
    {provide: VERSION_INFO, useValue: {key: AppConfig.versionTag, currentVersion: AppConfig.version}},
    VersionCheckHook,
    {provide: REQUEST_HOOK, useExisting: VersionCheckHook, multi: true},
    {provide: REMOTE_DATA_TYPE, useValue: EXT_ENUM, multi: true},
    {provide: REMOTE_DATA_TYPE, useValue: FAMILY_TYPE, multi: true},
    {provide: REMOTE_DATA_TYPE, useValue: NOT_ENSURED_FAMILY_TYPE, multi: true},
    {provide: EXT_ENUM_DATA_PROVIDER, useClass: AppExtEnumDataProvider},
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
