(function () {
  'use strict';

  //index
  var IndexCtrl = function ($scope, Model, SearchHelper) {
    function search(){
      Model.api.search($scope.form).then(function (data) {
        $scope.result = data;
      });
    }
    $scope.search = SearchHelper.getSearchFunc();

    Model.setSearchOptions($scope);
    SearchHelper.handle({
      init: function(){
        Model.api.searchForm().then(function (form) {
          $scope.form = form;
          search();
        });
      },
      resume: function(form){
        $scope.form = form;
        search();
      }
    });
  };

  //show
  var ShowCtrl = function ($scope, Model, $search, $url, CommonAlert, DownloadService) {
    $scope.delete = function (id) {
      if (!confirm('削除してもよろしいですか？')) return;
      Model.api.delete(id).then(function () {
        CommonAlert.reserveDeletedMessage();
        $url('./index', {}, true);
      });
    };

    $scope.download = function(id) {
      Model.api.download(id).then(DownloadService.downloadAt('photo'));
    };
    $scope.downloadFamilyCsv = function() {
      return Model.api.downloadFamilyCsv($search().id).then(DownloadService.downloadAt('csv'))
    };
    Model.api.record($search().id).then(function (data) {
      $scope.record = data;
    });
  };

  //edit
  var EditCtrl = function ($scope, Model, $search, $url, CommonAlert, DownloadService) {
    $scope.addFamily = function () {
      $scope.form.families.push({familyType: null});
    };
    $scope.save = function () {
      return Model.api.save($scope.form).then(function (data) {
        CommonAlert.reserveSavedMessage();
        $url('./show', {id: data.id});
      });
    };
    $scope.uploadFamilyCsv = function () {
      return Model.api.uploadFamilyCsv($scope.csvForm.family).then(function (data) {
        $scope.form.families = data;
        $scope.csvForm.family = null;
      });
    };

    Model.setEditOptions($scope);
    Model.api.form($search().id).then(function (form) {
      $scope.form = form;
    });
    $scope.csvForm = {family: null};
  };

  //route
  angular.module('app').config(function ($routeProvider, RESOURCE_ROOT, PARTIAL_PATH) {
    $routeProvider.when('/' + RESOURCE_ROOT + '/index', {
      templateUrl: PARTIAL_PATH.APP.INDEX,
      controller: IndexCtrl,
      options: ['sex']
    }).when('/' + RESOURCE_ROOT + '/show', {
      templateUrl: PARTIAL_PATH.APP.SHOW,
      controller: ShowCtrl,
      options: ['sex', 'familyType', 'infoSource']
    }).when('/' + RESOURCE_ROOT + '/edit', {
      templateUrl: PARTIAL_PATH.APP.EDIT,
      controller: EditCtrl,
      options: ['sex', 'familyType', 'infoSource']
    });
  });

}());
