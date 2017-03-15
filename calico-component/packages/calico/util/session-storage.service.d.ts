import { Injector } from '@angular/core';
import { SerializeService } from "./serialize.service";
export interface SessionStorageServiceConfig {
    storage?: Storage;
    prefix?: string;
    deployedAt?: string;
}
export declare class SessionStorageService {
    private serializeService;
    private sessionStorage;
    private prefix;
    private deployedAt;
    constructor(serializeService: SerializeService, injector: Injector);
    store(key: string, value: any): void;
    store(key: string, value: any, withDeployedAt: boolean): void;
    restore(key: string): any;
    restore(key: string, withDeployedAt: boolean): any;
    restoreRawData(key: string): string;
    restoreRawData(key: string, withDeployedAt: boolean): string;
    remove(key: string): void;
    remove(key: string, withDeployedAt: boolean): void;
    clear(): void;
    clear(matchKey: string): void;
    clear(matchKey: string, withDeployedAt: boolean): void;
    clearByDeployedAt(deployedAt: string): void;
    private keyPrefix(withDeployedAt?);
    private createKey(key, withDeployedAt?);
    private removeMatched(matchKey);
}
