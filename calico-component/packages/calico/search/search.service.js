var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Injectable, EventEmitter } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { SerializeService } from "../util/serialize.service";
import { SessionStorageService } from "../util/session-storage.service";
var SearchService = (function () {
    function SearchService(sessionStorageService, serializeService) {
        this.sessionStorageService = sessionStorageService;
        this.serializeService = serializeService;
    }
    SearchService.prototype.storeFormValue = function (key, value) {
        this.sessionStorageService.store(this.createKey(key), value);
    };
    SearchService.prototype.clearFormValue = function (key) {
        this.sessionStorageService.remove(this.createKey(key));
    };
    SearchService.prototype.restoreFormValue = function (key) {
        return this.sessionStorageService.restore(this.createKey(key));
    };
    SearchService.prototype.restoreAsFragment = function (key) {
        var formValue = this.restoreFormValue(key);
        if (formValue == null)
            return null;
        return this.serializeService.serialize(formValue);
    };
    SearchService.prototype.createKey = function (key) {
        return 'search-form-' + key;
    };
    return SearchService;
}());
SearchService = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [SessionStorageService,
        SerializeService])
], SearchService);
export { SearchService };
var SearchContext = (function () {
    function SearchContext(route, router, serializeService, searchService) {
        this.route = route;
        this.router = router;
        this.serializeService = serializeService;
        this.searchService = searchService;
        this.searching = false;
        this.searched = new EventEmitter();
        this._subscriptions = [];
    }
    Object.defineProperty(SearchContext.prototype, "subscription", {
        set: function (subription) {
            this._subscriptions.push(subription);
        },
        enumerable: true,
        configurable: true
    });
    SearchContext.prototype.init = function (config) {
        var _this = this;
        if (config.initialSearch == null) {
            config.initialSearch = false;
        }
        this.config = config;
        this.subscription = this.route.fragment.subscribe(function (fragment) {
            _this.lastFragment = fragment;
            if (fragment == null) {
                _this.config.getForm()
                    .subscribe(function (form) {
                    _this.form = form;
                    if (_this.config.initialSearch) {
                        _this.executeSearch();
                    }
                    else {
                        _this.result = null;
                    }
                });
            }
            else {
                _this.form = _this.config.toForm(_this.serializeService.deserialize(fragment));
                _this.executeSearch();
            }
        });
    };
    SearchContext.prototype.onDestroy = function () {
        this._subscriptions.forEach(function (s) { return s.unsubscribe(); });
        this._subscriptions = [];
    };
    SearchContext.prototype.search = function () {
        var fragment = this.serializeService.serialize(this.form.value);
        if (fragment == this.lastFragment) {
            this.executeSearch();
        }
        else {
            this.router.navigate([], { fragment: fragment });
        }
    };
    SearchContext.prototype.executeSearch = function () {
        var _this = this;
        this.searching = true;
        var formValue = Object.clone(this.form.value, true);
        this.config.search().subscribe(function (data) {
            _this.result = data;
            _this.searched.emit(data);
            _this.lastFormValue = formValue;
            if (_this.lastFragment == null) {
                _this.searchService.clearFormValue(_this.getKey());
            }
            else {
                _this.searchService.storeFormValue(_this.getKey(), formValue);
            }
            _this.searching = false;
        });
    };
    SearchContext.prototype.onPageNoChange = function (no) {
        var form = this.config.toForm(this.lastFormValue);
        var noControl = form.get('_page.no');
        noControl.setValue(no);
        this.form = form;
        this.search();
    };
    SearchContext.prototype.onPerPageChange = function (perPage) {
        var form = this.config.toForm(this.lastFormValue);
        var noControl = form.get('_page.no');
        var perPageControl = form.get('_page.perPage');
        noControl.setValue(1);
        perPageControl.setValue(perPage);
        this.form = form;
        this.search();
    };
    SearchContext.prototype.onSortChange = function (prop) {
        var form = this.config.toForm(this.lastFormValue);
        var propControl = form.get('_sort.prop');
        var typeControl = form.get('_sort.type');
        var noControl = form.get('_page.no');
        if (propControl.value == prop) {
            typeControl.setValue(typeControl.value == 'DESC' ? 'ASC' : 'DESC');
        }
        else {
            propControl.setValue(prop);
            typeControl.setValue('ASC');
        }
        if (noControl != null) {
            noControl.setValue(1);
        }
        this.form = form;
        this.search();
    };
    SearchContext.prototype.getKey = function () {
        return this.route.pathFromRoot
            .map(function (r) { return r.url.value; })
            .flatten()
            .map(function (v) { return v.path; })
            .filter(function (v) { return v != null && v != ''; })
            .join('/');
    };
    return SearchContext;
}());
SearchContext = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [ActivatedRoute,
        Router,
        SerializeService,
        SearchService])
], SearchContext);
export { SearchContext };
//# sourceMappingURL=search.service.js.map