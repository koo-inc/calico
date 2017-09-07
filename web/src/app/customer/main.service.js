"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var forms_1 = require("@angular/forms");
var MainService = (function () {
    function MainService(api, fb, sfb) {
        this.api = api;
        this.fb = fb;
        this.sfb = sfb;
    }
    MainService.prototype.submit = function (url, data) {
        return this.api.submit('endpoint/customer/' + url, data);
    };
    MainService.prototype.getSearchForm = function () {
        var _this = this;
        return this.submit('search_form', {})
            .map(function (data) { return _this.toSearchForm(data); });
    };
    MainService.prototype.toSearchForm = function (data) {
        return this.sfb.rootGroup(data, {
            name: [data.name],
            sex: [data.sex],
        });
    };
    MainService.prototype.search = function (form) {
        return this.submit('search', form);
    };
    MainService.prototype.getRecord = function (id) {
        return this.submit('record', { id: id });
    };
    MainService.prototype.getEditForm = function (id) {
        return id == null ? this.getCreateForm() : this.getUpdateForm(id);
    };
    MainService.prototype.getCreateForm = function () {
        var _this = this;
        return this.submit('create_form', {})
            .map(function (data) { return _this.toEditForm(data); });
    };
    MainService.prototype.getUpdateForm = function (id) {
        var _this = this;
        return this.submit('update_form', { id: id })
            .map(function (data) { return _this.toEditForm(data); });
    };
    MainService.prototype.toEditForm = function (data) {
        var _this = this;
        return this.fb.group({
            id: [data.id],
            kname1: [data.kname1, forms_1.Validators.required],
            kname2: [data.kname2, forms_1.Validators.required],
            fname1: [data.fname1],
            fname2: [data.fname1],
            sex: [data.sex],
            favoriteNumber: [data.favoriteNumber],
            claimer: [data.claimer],
            birthday: [data.birthday],
            contactEnableStartTime: [data.contactEnableStartTime],
            contactEnableEndTime: [data.contactEnableEndTime],
            email: [data.email],
            homepageUrl: [data.homepageUrl],
            phoneNumber: [data.phoneNumber],
            // photo: [data.photo],
            families: this.fb.array(data.families.map(function (family) { return _this.toEditFamilyForm(family); })),
        });
    };
    MainService.prototype.toEditFamilyForm = function (family) {
        return this.fb.group({
            id: [family.id],
            familyType: [family.familyType],
            name: [family.name, forms_1.Validators.required],
            sex: [family.sex],
            favoriteNumber: [family.favoriteNumber],
            birthday: [family.birthday],
        });
    };
    MainService.prototype.addFamily = function (form) {
        var families = form.get('families');
        families.push(this.toEditFamilyForm({}));
    };
    MainService.prototype.removeFamily = function (form, index) {
        var families = form.get('families');
        families.removeAt(index);
    };
    MainService.prototype.save = function (form) {
        return form.value.id == null ? this.create(form) : this.update(form);
    };
    MainService.prototype.create = function (form) {
        return this.submit('create', form);
    };
    MainService.prototype.update = function (form) {
        return this.submit('update', form);
    };
    MainService.prototype.delete = function (id) {
        return this.submit('delete', { id: id });
    };
    MainService.prototype.getUploadForm = function () {
        return this.fb.group({
            csv: [null, forms_1.Validators.required]
        });
    };
    MainService.prototype.upload = function (form) {
        return this.submit('upload', form);
    };
    MainService.prototype.download = function (form) {
        return this.submit('download_customers', form)
            .map(function (data) { return data.csv; });
    };
    MainService = __decorate([
        core_1.Injectable()
    ], MainService);
    return MainService;
}());
exports.MainService = MainService;
var Record = (function () {
    function Record() {
    }
    return Record;
}());
exports.Record = Record;
