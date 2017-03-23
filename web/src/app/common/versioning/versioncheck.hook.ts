import { Observable } from "rxjs";
import { Injectable, OpaqueToken, Inject } from "@angular/core";
import { Response } from "@angular/http";
import { Router, NavigationStart } from "@angular/router";
import { RequestHook } from "calico/core/api.service";

export const VERSION_INFO = new OpaqueToken("versionInfo");

export interface VersionInfo {
  key: string;
  currentVersion: string;
}

@Injectable()
export class VersionCheckHook implements RequestHook {
  constructor(
    @Inject(VERSION_INFO) private versionInfo: VersionInfo,
    private router: Router
  ) {
  }
  apply(url: string, form: any, observable: Observable<Response>): Observable<Response> {
      return observable.do(res => {
        let version = res.headers.get(this.versionInfo.key);
        if (version == null || version == this.versionInfo.currentVersion) return;
        this.router.events
          .filter(e => e instanceof NavigationStart)
          .subscribe(loc => location.href = loc.url);
      });
  }
}
