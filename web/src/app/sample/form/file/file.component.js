"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var forms_1 = require("@angular/forms");
var index_component_1 = require("app/sample/form/index/index.component");
var Observable_1 = require("rxjs/Observable");
var file_1 = require("calico/util/file");
var FileComponent = (function (_super) {
    __extends(FileComponent, _super);
    function FileComponent(alert, extEnumService, fb, api) {
        var _this = _super.call(this, alert, extEnumService) || this;
        _this.fb = fb;
        _this.api = api;
        return _this;
    }
    FileComponent.prototype.createForm = function () {
        var form = this.fb.group({
            val1: [null],
            val2: [{ meta: { name: 'test1', size: 10, type: 'text/plain' }, payload: null }],
            val3: [null, forms_1.Validators.required],
            val4: [{ meta: { name: 'test2', size: 1000, type: 'text/plain' }, payload: null }, function (control) {
                    if (control.value != null && control.value.meta != null && control.value.meta.size >= 100) {
                        return { '100バイト以上です': true };
                    }
                    return {};
                }],
            val5: [null],
        });
        setTimeout(function () {
            form.get('val5').patchValue({ meta: { name: 'test3', size: 1000, type: 'text/plain' }, payload: null });
        }, 3000);
        return Observable_1.Observable.of(form);
    };
    FileComponent.prototype.download = function (media) {
        this.api.submit('endpoint/sample/form/echo_file', { media: media }).subscribe(function (data) {
            file_1.download(data.media.payload, data.media.meta.name);
        });
    };
    FileComponent = __decorate([
        core_1.Component({
            selector: 'app-file',
            templateUrl: './file.component.html',
            styles: ["\n    .large {\n      width: 250px;\n    }\n  "]
        })
    ], FileComponent);
    return FileComponent;
}(index_component_1.DefaultFormComponent));
exports.FileComponent = FileComponent;
