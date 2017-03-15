import { Injectable, Injector } from '@angular/core';
import {Observable} from "rxjs";

import { Api } from "calico";
import { LocalStorageService } from "calico";

export interface ExtEnumServiceConfig {
  apiPath: string;
  cacheKeyPrefix?: string;
}

@Injectable()
export class ExtEnumService {
  private apiPath: string;
  private cacheKeyPrefix: string = 'extenum:';

  constructor(
    private api: Api,
    private localStorageService: LocalStorageService,
    injector: Injector,
  ) {
    let config: ExtEnumServiceConfig = injector.get("ExtEnumServiceConfig", null);
    if (config != null) {
      this.apiPath = config.apiPath;
      if(config.cacheKeyPrefix != null){
        this.cacheKeyPrefix = config.cacheKeyPrefix;
      }
    }
    if(this.apiPath == null){
      throw 'ExtEnumService.apiPath is undefined';
    }
  }

  values(...keys: string[]): Observable<ExtEnumData> {
    let keysForApi: string[] = [];
    let cachedData: ExtEnumData = new ExtEnumData();
    for(let key of keys){
      let cache = this.localStorageService.restore(this.createCacheKey(key), true);
      if(cache){
        cachedData[key] = cache;
      }else{
        keysForApi.push(key);
      }
    }
    return this.fromApi(keysForApi).do(data => {
      for(let key in data){
        this.localStorageService.store(this.createCacheKey(key), data[key], true);
      }
      for(let key in cachedData){
        data[key] = cachedData[key];
      }
    });
  }

  private fromApi(keys: string[]): Observable<ExtEnumData> {
    if(keys.length == 0){
      return Observable.of(new ExtEnumData());
    }
    return this.api.submit(this.apiPath, {keys: keys});
  }

  private createCacheKey(key: string): string {
    return this.cacheKeyPrefix + key;
  }
}

export class ExtEnum {
  NAME: string;
  id: any;
  name: string;
}

export class ExtEnumData {
  [key:string]: ExtEnum[];
}
