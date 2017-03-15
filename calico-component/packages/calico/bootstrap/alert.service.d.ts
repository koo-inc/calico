import { Injector } from "@angular/core";
export declare class AlertService {
    private injector;
    constructor(injector: Injector);
    config: AlertConfig;
    messages: {
        [key: string]: AlertMessage[];
    };
    success(body: string, opts?: AlertOptions): void;
    info(body: string, opts?: AlertOptions): void;
    warning(body: string, opts?: AlertOptions): void;
    danger(body: string, opts?: AlertOptions): void;
    private createMessage(type, body, opts?);
    private normalizePosition(position);
    private addMessage(message);
    private removeMessage(message);
}
export interface AlertOptions {
    position?: string;
    lifetime?: number;
}
export interface AlertConfig {
    common?: AlertOptions;
    success?: AlertOptions;
    info?: AlertOptions;
    warning?: AlertOptions;
    danger?: AlertOptions;
}
export interface AlertMessage {
    type: string;
    body: string;
    position: string;
    lifetime: number;
    key: string;
    state: string;
}
export declare class AlertComponent {
    private alertService;
    constructor(alertService: AlertService);
    identify(index: number, message: any): any;
}
