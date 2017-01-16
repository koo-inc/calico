import { EventEmitter } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { FormGroup } from "@angular/forms";
import { Observable, Subscription } from "rxjs";
import { SerializeService } from "../util/serialize.service";
import { SessionStorageService } from "../util/session-storage.service";
export declare class SearchService {
    private sessionStorageService;
    private serializeService;
    constructor(sessionStorageService: SessionStorageService, serializeService: SerializeService);
    storeFormValue(key: string, value: any): void;
    clearFormValue(key: string): void;
    restoreFormValue(key: string): any;
    restoreAsFragment(key: string): string;
    createKey(key: string): string;
}
export declare class SearchContext {
    private route;
    private router;
    private serializeService;
    private searchService;
    constructor(route: ActivatedRoute, router: Router, serializeService: SerializeService, searchService: SearchService);
    config: SearchContextConfig;
    form: FormGroup;
    result: any;
    searching: boolean;
    searched: EventEmitter<any>;
    private lastFragment;
    private lastFormValue;
    _subscriptions: Subscription[];
    subscription: Subscription;
    init(config: SearchContextConfig): void;
    onDestroy(): void;
    search(): void;
    private executeSearch();
    onPageNoChange(no: number): void;
    onPerPageChange(perPage: number): void;
    onSortChange(prop: string): void;
    getKey(): string;
}
export interface SearchContextConfig {
    search: () => Observable<any>;
    getForm: () => Observable<any>;
    toForm: (form: any) => FormGroup;
    initialSearch?: boolean;
}
