var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { Injectable } from '@angular/core';
var GrowlService = (function () {
    function GrowlService() {
        this.messages = [];
    }
    GrowlService.prototype.push = function (severity, summary, detail) {
        this.messages.push({ severity: severity, summary: summary, detail: detail });
    };
    GrowlService.prototype.success = function (summary, detail) {
        this.push('success', summary, detail);
    };
    GrowlService.prototype.info = function (summary, detail) {
        this.push('info', summary, detail);
    };
    GrowlService.prototype.warn = function (summary, detail) {
        this.push('warn', summary, detail);
    };
    GrowlService.prototype.error = function (summary, detail) {
        this.push('error', summary, detail);
    };
    GrowlService.prototype.savedMessage = function () {
        this.success('保存しました。');
    };
    GrowlService.prototype.deletedMessage = function () {
        this.success('削除しました。');
    };
    return GrowlService;
}());
GrowlService = __decorate([
    Injectable()
], GrowlService);
export { GrowlService };
//# sourceMappingURL=growl.service.js.map