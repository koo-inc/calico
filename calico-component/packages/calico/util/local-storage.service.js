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
var LocalStorageService = (function () {
    function LocalStorageService(serializeService, injector) {
        this.serializeService = serializeService;
        var config = injector.get("LocalStorageServiceConfig", null);
        if (config != null) {
            this.localStorage = config.storage;
            this.prefix = config.prefix;
            this.deployedAt = config.deployedAt;
        }
        if (this.localStorage == null && 'localStorage' in global) {
            this.localStorage = localStorage;
        }
        else {
            throw new Error("this browser doesn't support Local Storage.");
        }
        if (this.prefix == null) {
            this.prefix = '';
        }
        if (this.deployedAt == null) {
            this.deployedAt = Date.now() + '';
        }
    }
    LocalStorageService.prototype.store = function (key, value, withDeployedAt) {
        key = this.createKey(key, withDeployedAt);
        this.localStorage.setItem(key, this.serializeService.serialize(value));
    };
    LocalStorageService.prototype.restore = function (key, withDeployedAt) {
        return this.serializeService.deserialize(this.restoreRawData(key, withDeployedAt));
    };
    LocalStorageService.prototype.restoreRawData = function (key, withDeployedAt) {
        key = this.createKey(key, withDeployedAt);
        return this.localStorage.getItem(key);
    };
    LocalStorageService.prototype.remove = function (key, withDeployedAt) {
        key = this.createKey(key, withDeployedAt);
        this.localStorage.removeItem(key);
    };
    LocalStorageService.prototype.clear = function (matchKey, withDeployedAt) {
        matchKey = this.createKey(matchKey == null ? '' : matchKey, withDeployedAt);
        this.removeMatched(matchKey);
    };
    LocalStorageService.prototype.clearByDeployedAt = function (deployedAt) {
        this.removeMatched(this.prefix + deployedAt + '-');
    };
    LocalStorageService.prototype.keyPrefix = function (withDeployedAt) {
        return withDeployedAt === true ? this.prefix + this.deployedAt + '-' : this.prefix;
    };
    LocalStorageService.prototype.createKey = function (key, withDeployedAt) {
        return this.keyPrefix(withDeployedAt) + key;
    };
    LocalStorageService.prototype.removeMatched = function (matchKey) {
        var _this = this;
        if (matchKey == '') {
            this.localStorage.clear();
            return;
        }
        var keys = [];
        for (var i = 0; i < this.localStorage.length; i++) {
            keys.push(this.localStorage.key(i));
        }
        keys.forEach(function (key) {
            if (key.startsWith(matchKey)) {
                _this.localStorage.removeItem(key);
            }
        });
    };
    return LocalStorageService;
}());
LocalStorageService = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [SerializeService, Injector])
], LocalStorageService);
export { LocalStorageService };
//# sourceMappingURL=local-storage.service.js.map