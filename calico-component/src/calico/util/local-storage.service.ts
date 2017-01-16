import {Injectable, Injector} from '@angular/core';
import {SerializeService} from "./serialize.service";

export interface LocalStorageServiceConfig {
  storage?: Storage;
  prefix?: string;
  deployedAt?: string;
}

@Injectable()
export class LocalStorageService {
  private localStorage: Storage;
  private prefix: string;
  private deployedAt: string;

  constructor(private serializeService: SerializeService, injector: Injector) {
    let config: LocalStorageServiceConfig = injector.get("LocalStorageServiceConfig", null);
    if (config != null) {
      this.localStorage = config.storage;
      this.prefix = config.prefix;
      this.deployedAt = config.deployedAt;
    }

    if (this.localStorage == null && 'localStorage' in global) {
      this.localStorage = localStorage;
    }
    else {
      throw new Error("this browser doesn't support Local Storage.")
    }
    if (this.prefix == null) {
      this.prefix = '';
    }

    if (this.deployedAt == null) {
      this.deployedAt = Date.now() + '';
    }
  }

  store(key: string, value: any): void;
  store(key: string, value: any, withDeployedAt: boolean): void;
  store(key: string, value: any, withDeployedAt?: boolean): void {
    key = this.createKey(key, withDeployedAt);
    this.localStorage.setItem(key, this.serializeService.serialize(value));
  }
  restore(key: string): any;
  restore(key: string, withDeployedAt: boolean): any;
  restore(key: string, withDeployedAt?: boolean): any {
    return this.serializeService.deserialize(this.restoreRawData(key, withDeployedAt));
  }
  restoreRawData(key: string): string;
  restoreRawData(key: string, withDeployedAt: boolean): string;
  restoreRawData(key: string, withDeployedAt?: boolean): string {
    key = this.createKey(key, withDeployedAt);
    return this.localStorage.getItem(key);
  }

  remove(key: string): void;
  remove(key: string, withDeployedAt: boolean): void;
  remove(key: string, withDeployedAt?: boolean): void {
    key = this.createKey(key, withDeployedAt);
    this.localStorage.removeItem(key);
  }

  clear(): void;
  clear(matchKey: string): void;
  clear(matchKey: string, withDeployedAt: boolean): void;
  clear(matchKey?: string, withDeployedAt?: boolean): void {
    matchKey = this.createKey(matchKey == null ? '' : matchKey, withDeployedAt);
    this.removeMatched(matchKey);
  }

  clearByDeployedAt(deployedAt: string) {
    this.removeMatched(this.prefix + deployedAt + '-');
  }

  private keyPrefix(withDeployedAt?: boolean): string {
    return withDeployedAt === true ? this.prefix + this.deployedAt + '-' : this.prefix;
  }
  private createKey(key: string, withDeployedAt?: boolean): string {
    return this.keyPrefix(withDeployedAt) + key;
  }

  private removeMatched(matchKey: string): void {
    if (matchKey == '') {
      this.localStorage.clear();
      return;
    }
    let keys: string[] = [];
    for(let i = 0; i < this.localStorage.length; i++) {
      keys.push(this.localStorage.key(i));
    }
    keys.forEach(key => {
      if (key.startsWith(matchKey)) {
        this.localStorage.removeItem(key);
      }
    });
  }
}

