import { Injectable, Component, trigger, state, style, transition, animate, keyframes, Injector } from "@angular/core";
import { randomString } from "../util/string";

@Injectable()
export class AlertService {
  constructor(
    private injector: Injector
  ) {
    this.config = this.injector.get('AlertConfig', {});
  }

  config: AlertConfig;
  messages: {[key:string]: AlertMessage[]} = {
    'top-left': [],
    'top-right': [],
    'bottom-left': [],
    'bottom-right': [],
  };

  success(body: string, opts?: AlertOptions): void {
    this.addMessage(this.createMessage('success', body, opts));
  }

  info(body: string, opts?: AlertOptions): void {
    this.addMessage(this.createMessage('info', body, opts));
  }

  warning(body: string, opts?: AlertOptions): void {
    this.addMessage(this.createMessage('warning', body, opts));
  }

  danger(body: string, opts?: AlertOptions): void {
    this.addMessage(this.createMessage('danger', body, opts));
  }

  private createMessage(type: string, body: string, opts?: AlertOptions): AlertMessage {
    let message: AlertMessage = Object.assign(
      {},
      this.config.common,
      this.config[type],
      opts,
      { type: type, body: body },
    ) as AlertMessage;
    message.position = this.normalizePosition(message.position);
    message.key = randomString(8);
    message.state = message.position.indexOf('left') != -1 ? 'in-left' : 'in-right';
    return message;
  }
  private normalizePosition(position: string): string {
    let v = position == null || position.indexOf('bottom') == -1 ? 'top' : 'bottom';
    let h = position == null || position.indexOf('left') == -1 ? 'right' : 'left';
    return v + '-' + h;
  }

  private addMessage(message: AlertMessage): void {
    if(message.position.indexOf('top') != -1){
      this.messages[message.position].unshift(message);
    }else{
      this.messages[message.position].push(message);
    }
    if(message.lifetime != null && message.lifetime >= 0){
      setTimeout(() => {
        this.removeMessage(message);
      }, message.lifetime);
    }
  }

  private removeMessage(message: any): void {
    this.messages[message.position].remove((e) => e.key == message.key);
  }
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

@Component({
  selector: 'c-alert',
  template: `
    <div class="c-alert">
      <div class="alert-container top left">
        <template [ngTemplateOutlet]="tpl" [ngOutletContext]="{position: 'top-left'}"></template>
      </div>
      <div class="alert-container top right">
        <template [ngTemplateOutlet]="tpl" [ngOutletContext]="{position: 'top-right'}"></template>
      </div>
      <div class="alert-container bottom left">
        <template [ngTemplateOutlet]="tpl" [ngOutletContext]="{position: 'bottom-left'}"></template>
      </div>
      <div class="alert-container bottom right">
        <template [ngTemplateOutlet]="tpl" [ngOutletContext]="{position: 'bottom-right'}"></template>
      </div>
    </div>
    <template #tpl let-position="position">
      <div *ngFor="let message of alertService.messages[position];trackBy: identify"
          class="alert alert-{{message.type}}"
          [@state]="message.state">
        <a class="close" (click)="alertService.removeMessage(message)">×</a>
        {{message.body}}
      </div>
    </template>
  `,
  styles: [`
  `],
  animations: [
    trigger('state', [
      transition('void => in-left', [
        animate(300, keyframes([
          style({
            height: 0,
            padding: 0,
            margin: 0,
            transform: 'translateX(-100%)',
            opacity: 0,
            offset: 0,
          }),
          style({
            height: '*',
            padding: '*',
            margin: '*',
            offset: 0.3,
          }),
          style({
            transform: 'translateX(0)',
            opacity: 0.9,
            offset: 1.0,
          }),
        ]))
      ]),
      transition('void => in-right', [
        animate(300, keyframes([
          style({
            height: 0,
            padding: 0,
            margin: 0,
            transform: 'translateX(100%)',
            opacity: 0,
            offset: 0,
          }),
          style({
            height: '*',
            padding: '*',
            margin: '*',
            offset: 0.3,
          }),
          style({
            transform: 'translateX(0)',
            opacity: 0.9,
            offset: 1.0,
          }),
        ]))
      ]),
      transition('* => void', [
        animate(300, keyframes([
          style({
            offset: 0,
          }),
          style({
            opacity: 0,
            height: '*',
            padding: '*',
            margin: '*',
            offset: 0.7,
          }),
          style({
            height: 0,
            padding: 0,
            margin: 0,
            offset: 1.0,
          }),
        ]))
      ]),
    ])
  ]
})
export class AlertComponent {
  constructor(
    private alertService: AlertService,
  ) {}

  identify(index: number, message: any): any {
    return message.key;
  }
}
