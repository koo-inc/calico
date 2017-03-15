var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Injectable, Injector } from '@angular/core';
import { SerializeService } from "./serialize.service";
var SessionStorageService = (function () {
    function SessionStorageService(serializeService, injector) {
        this.serializeService = serializeService;
        var config = injector.get("SessionStorageServiceConfig", null);
        if (config != null) {
            this.sessionStorage = config.storage;
            this.prefix = config.prefix;
            this.deployedAt = config.deployedAt;
        }
        if (this.sessionStorage == null && 'sessionStorage' in global) {
            this.sessionStorage = sessionStorage;
        }
        else {
            throw new Error("this browser doesn't support Session Storage.");
        }
        if (this.prefix == null) {
            this.prefix = '';
        }
        if (this.deployedAt == null) {
            this.deployedAt = Date.now() + '';
        }
    }
    SessionStorageService.prototype.store = function (key, value, withDeployedAt) {
        key = this.createKey(key, withDeployedAt);
        this.sessionStorage.setItem(key, this.serializeService.serialize(value));
    };
    SessionStorageService.prototype.restore = function (key, withDeployedAt) {
        return this.serializeService.deserialize(this.restoreRawData(key, withDeployedAt));
    };
    SessionStorageService.prototype.restoreRawData = function (key, withDeployedAt) {
        key = this.createKey(key, withDeployedAt);
        return this.sessionStorage.getItem(key);
    };
    SessionStorageService.prototype.remove = function (key, withDeployedAt) {
        key = this.createKey(key, withDeployedAt);
        this.sessionStorage.removeItem(key);
    };
    SessionStorageService.prototype.clear = function (matchKey, withDeployedAt) {
        matchKey = this.createKey(matchKey == null ? '' : matchKey, withDeployedAt);
        this.removeMatched(matchKey);
    };
    SessionStorageService.prototype.clearByDeployedAt = function (deployedAt) {
        this.removeMatched(this.prefix + deployedAt + '-');
    };
    SessionStorageService.prototype.keyPrefix = function (withDeployedAt) {
        return withDeployedAt === true ? this.prefix + this.deployedAt + '-' : this.prefix;
    };
    SessionStorageService.prototype.createKey = function (key, withDeployedAt) {
        return this.keyPrefix(withDeployedAt) + key;
    };
    SessionStorageService.prototype.removeMatched = function (matchKey) {
        var _this = this;
        if (matchKey == '') {
            this.sessionStorage.clear();
            return;
        }
        var keys = [];
        for (var i = 0; i < this.sessionStorage.length; i++) {
            keys.push(this.sessionStorage.key(i));
        }
        keys.forEach(function (key) {
            if (key.startsWith(matchKey)) {
                _this.sessionStorage.removeItem(key);
            }
        });
    };
    return SessionStorageService;
}());
SessionStorageService = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [SerializeService, Injector])
], SessionStorageService);
export { SessionStorageService };
//# sourceMappingURL=session-storage.service.js.map