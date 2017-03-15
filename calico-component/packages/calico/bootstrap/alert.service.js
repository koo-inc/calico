var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Injectable, Component, trigger, style, transition, animate, keyframes, Injector } from "@angular/core";
import { randomString } from "../util/string";
var AlertService = (function () {
    function AlertService(injector) {
        this.injector = injector;
        this.messages = {
            'top-left': [],
            'top-right': [],
            'bottom-left': [],
            'bottom-right': [],
        };
        this.config = this.injector.get('AlertConfig', {});
    }
    AlertService.prototype.success = function (body, opts) {
        this.addMessage(this.createMessage('success', body, opts));
    };
    AlertService.prototype.info = function (body, opts) {
        this.addMessage(this.createMessage('info', body, opts));
    };
    AlertService.prototype.warning = function (body, opts) {
        this.addMessage(this.createMessage('warning', body, opts));
    };
    AlertService.prototype.danger = function (body, opts) {
        this.addMessage(this.createMessage('danger', body, opts));
    };
    AlertService.prototype.createMessage = function (type, body, opts) {
        var message = Object.assign({}, this.config.common, this.config[type], opts, { type: type, body: body });
        message.position = this.normalizePosition(message.position);
        message.key = randomString(8);
        message.state = message.position.indexOf('left') != -1 ? 'in-left' : 'in-right';
        return message;
    };
    AlertService.prototype.normalizePosition = function (position) {
        var v = position == null || position.indexOf('bottom') == -1 ? 'top' : 'bottom';
        var h = position == null || position.indexOf('left') == -1 ? 'right' : 'left';
        return v + '-' + h;
    };
    AlertService.prototype.addMessage = function (message) {
        var _this = this;
        if (message.position.indexOf('top') != -1) {
            this.messages[message.position].unshift(message);
        }
        else {
            this.messages[message.position].push(message);
        }
        if (message.lifetime != null && message.lifetime >= 0) {
            setTimeout(function () {
                _this.removeMessage(message);
            }, message.lifetime);
        }
    };
    AlertService.prototype.removeMessage = function (message) {
        this.messages[message.position].remove(function (e) { return e.key == message.key; });
    };
    return AlertService;
}());
AlertService = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [Injector])
], AlertService);
export { AlertService };
var AlertComponent = (function () {
    function AlertComponent(alertService) {
        this.alertService = alertService;
    }
    AlertComponent.prototype.identify = function (index, message) {
        return message.key;
    };
    return AlertComponent;
}());
AlertComponent = __decorate([
    Component({
        selector: 'c-alert',
        template: "\n    <div class=\"c-alert\">\n      <div class=\"alert-container top left\">\n        <template [ngTemplateOutlet]=\"tpl\" [ngOutletContext]=\"{position: 'top-left'}\"></template>\n      </div>\n      <div class=\"alert-container top right\">\n        <template [ngTemplateOutlet]=\"tpl\" [ngOutletContext]=\"{position: 'top-right'}\"></template>\n      </div>\n      <div class=\"alert-container bottom left\">\n        <template [ngTemplateOutlet]=\"tpl\" [ngOutletContext]=\"{position: 'bottom-left'}\"></template>\n      </div>\n      <div class=\"alert-container bottom right\">\n        <template [ngTemplateOutlet]=\"tpl\" [ngOutletContext]=\"{position: 'bottom-right'}\"></template>\n      </div>\n    </div>\n    <template #tpl let-position=\"position\">\n      <div *ngFor=\"let message of alertService.messages[position];trackBy: identify\"\n          class=\"alert alert-{{message.type}}\"\n          [@state]=\"message.state\">\n        <a class=\"close\" (click)=\"alertService.removeMessage(message)\">\u00D7</a>\n        {{message.body}}\n      </div>\n    </template>\n  ",
        styles: ["\n  "],
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
    }),
    __metadata("design:paramtypes", [AlertService])
], AlertComponent);
export { AlertComponent };
//# sourceMappingURL=alert.service.js.map