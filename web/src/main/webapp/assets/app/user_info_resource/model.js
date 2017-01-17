(function() {
  'use strict';

  angular.module('app').factory('Model', function(ApiService) {
    return {
      api: {
        records: function() {
          return ApiService.post('userinfo/records', {});
        },
        createForm: function() {
          return ApiService.post('userinfo/create_form', {});
        },
        updateForm: function(id) {
          return ApiService.post('userinfo/update_form', {id: id});
        },
        form: function(id){
          return id == null ? this.createForm() : this.updateForm(id);
        },
        create: function(form) {
          return ApiService.post('userinfo/create', form);
        },
        update: function(form) {
          return ApiService.post('userinfo/update', form);
        },
        save: function(form){
          return form.id == null ? this.create(form) : this.update(form);
        },
        delete: function(id) {
          return ApiService.post('userinfo/delete', {id: id});
        },
        isUniqueLoginId: function(loginId, exceptId){
          return ApiService.post('userinfo/is_unique_login_id', {loginId: loginId, exceptId: exceptId});
        }
      }
    };
  });
}());
