import { Observable } from "rxjs";
import { filter, map, tap } from 'rxjs/operators';
import { Injectable, Inject, InjectionToken } from "@angular/core";
import { Router, NavigationStart } from "@angular/router";
import { RequestHook } from "calico/core/api.service";
import { HttpResponse } from "@angular/common/http";

export const VERSION_INFO = new InjectionToken<VersionInfo>("versionInfo");

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
  apply(url: string, form: any, observable: Observable<HttpResponse<any>>): Observable<HttpResponse<any>> {
    return tap((res: HttpResponse<any>) => {
      let version = res.headers.get(this.versionInfo.key);
      if (version == null || version == this.versionInfo.currentVersion) return;
      this.router.events.pipe(
        filter(e => e instanceof NavigationStart),
        map(e => e as NavigationStart),
      ).subscribe(loc => location.href = loc.url);
    })(observable);
  }
}
