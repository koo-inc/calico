import { Message } from "primeng/primeng";
export declare class GrowlService {
    messages: Message[];
    push(severity: string, summary: string, detail?: string): void;
    success(summary: string, detail?: string): void;
    info(summary: string, detail?: string): void;
    warn(summary: string, detail?: string): void;
    error(summary: string, detail?: string): void;
    savedMessage(): void;
    deletedMessage(): void;
}
