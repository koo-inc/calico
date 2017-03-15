import { Injectable } from '@angular/core';
import { Message } from "primeng/primeng";

@Injectable()
export class GrowlService {
  messages: Message[] = [];

  push(severity: string, summary: string, detail?: string): void {
    this.messages.push({severity: severity, summary: summary, detail: detail});
  }

  success(summary: string, detail?: string){
    this.push('success', summary, detail);
  }

  info(summary: string, detail?: string){
    this.push('info', summary, detail);
  }

  warn(summary: string, detail?: string){
    this.push('warn', summary, detail);
  }

  error(summary: string, detail?: string){
    this.push('error', summary, detail);
  }

  savedMessage(){
    this.success('保存しました。');
  }

  deletedMessage(){
    this.success('削除しました。');
  }

}
