var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
import 'rxjs/add/operator/map';
import { Injectable, Inject, OpaqueToken } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Http, Headers, RequestOptions } from "@angular/http";
import { AlertService } from "../bootstrap/alert.service";
var MessageConfig = (function () {
    function MessageConfig() {
    }
    return MessageConfig;
}());
export { MessageConfig };
export var MESSAGE_CONFIG = new OpaqueToken('MessageConfig');
var Api = (function () {
    function Api(http, alert, messages) {
        this.http = http;
        this.alert = alert;
        this.messages = messages;
    }
    Api.prototype.submit = function (url, form) {
        var _this = this;
        var body = JSON.stringify(form instanceof FormGroup ? form.value : form);
        var headers = new Headers();
        headers.append('Content-Type', 'application/json');
        var options = new RequestOptions({ headers: headers });
        var req = this.http.post(url, body, options)
            .map(function (req, _) { return req.json(); });
        if (form instanceof FormGroup) {
            return req.catch(function (e, caught) {
                var errors;
                try {
                    errors = e.json();
                }
                catch (e) {
                    console.error(e);
                    _this.alert.warning(_this.messages['internalServerError'] || '500 Internal Server Error');
                    throw e;
                }
                Object.keys(errors).forEach(function (key) {
                    var violation = errors[key].reduce(function (a, b) { a[b] = true; return a; }, {});
                    var ctrl = form.get(key);
                    if (ctrl != null) {
                        ctrl.setErrors(violation);
                    }
                    else {
                        var message = errors[key].map(function (msg) { return _this.messages[msg] || msg; }).join('\n');
                        _this.alert.warning(message);
                    }
                });
                throw e;
            });
        }
        else {
            return req.catch(function (e, caught) {
                var errors;
                try {
                    e.json();
                }
                catch (e) {
                    console.error(e);
                    _this.alert.warning(_this.messages['internalServerError'] || '500 Internal Server Error');
                }
                throw e;
            });
        }
    };
    return Api;
}());
Api = __decorate([
    Injectable(),
    __param(2, Inject(MESSAGE_CONFIG)),
    __metadata("design:paramtypes", [Http, AlertService, MessageConfig])
], Api);
export { Api };
//# sourceMappingURL=api.service.js.map