(function() {
  'use strict';

  angular.module('app').factory('Model', function(ApiService, OptionsService) {
    return {
      api: {
        searchForm: function() {
          return ApiService.post('./search_form', {});
        },
        search: function(form) {
          return ApiService.post('./search', form);
        },
        record: function(id) {
          return ApiService.post('./record', {id: id});
        },
        createForm: function() {
          return ApiService.post('./create_form', {});
        },
        updateForm: function(id) {
          return ApiService.post('./update_form', {id: id});
        },
        form: function(id){
          return id == null ? this.createForm() : this.updateForm(id);
        },
        create: function(form) {
          return ApiService.post('./create', form);
        },
        update: function(form) {
          return ApiService.post('./update', form);
        },
        save: function(form){
          return form.id == null ? this.create(form) : this.update(form);
        },
        delete: function(id) {
          return ApiService.post('./delete', {id: id});
        },
        download: function(id) {
          return ApiService.post('./download', {id: id});
        },
        downloadFamilyCsv: function(id) {
          return ApiService.post('./download_family_csv', {id: id});
        },
        uploadFamilyCsv: function(form) {
          return ApiService.post('./upload_family_csv', form);
        },
        //テスト用
        randomCreate: function(){
          return ApiService.post('./random_create', {})
        }
      },
      setSearchOptions: function($scope){
        OptionsService.values('sex').then(function(data){
          $scope.sexes = data.sex.include({id: null, name: '不明', longName: '不明'}, 0);
        });
      },
      setEditOptions: function($scope){
        OptionsService.values('sex', 'familyType', 'infoSource').then(function(data){
          $scope.sexes = data.sex.include({id: null, name: '不明', longName: '不明'}, 0);
          $scope.familyTypes = data.familyType;
          $scope.infoSource = data.infoSource;
        });
      }
    };
  });
}());
