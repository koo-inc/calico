(function () {
  'use strict';

  //index
  var IndexCtrl = function ($scope, Model, ModalService) {
    function search(){
      Model.api.records().then(function (data) {
        $scope.records = data;
      });
    }
    $scope.openEditModal = function(record) {
      ModalService.open('ユーザ情報', $scope.PARTIAL_PATH.APP.EDIT_MODAL, {id: record ? record.id : null, onChange: function(){
        search();
      }});
    };

    search();
  };

  //edit_modal
  angular.module('app').controller('EditModalCtrl', function($scope, Model, CommonAlert){
    $scope.save = function () {
      return Model.api.save($scope.form).then(function () {
        CommonAlert.showSavedMessage();
        $scope.onChange();
        $scope.$hide();
      });
    };
    $scope.delete = function () {
      if (!confirm('削除してもよろしいですか？')) return;
      Model.api.delete($scope.form.id).then(function () {
        CommonAlert.showDeletedMessage();
        $scope.onChange();
        $scope.$hide();
      });
    };

    Model.api.form($scope.id).then(function (data) {
      $scope.form = data;
    });
  });

  //loginIdユニークチェック
  angular.module('app').directive('uniqueLoginId', function(Model, AsyncValidatorService){
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function (scope, element, attrs, ngModelCtrl) {
        var exceptId = scope.$eval(attrs.exceptId);
        scope.$watch(attrs.exceptId, function(val){
          exceptId = val;
        });
        ngModelCtrl.$asyncValidators.uniqueLoginId = AsyncValidatorService.createValidator(ngModelCtrl, function(modelValue, viewValue) {
          return Model.api.isUniqueLoginId(viewValue, exceptId);
        });
      }
    };
  }).run(function(ValidationMessageService){
    ValidationMessageService.addMessages({
      uniqueLoginId: '既に使用されているログインIDです。'
    });
  });

  //route
  angular.module('app').config(function ($routeProvider, RESOURCE_ROOT, PARTIAL_PATH) {
    $routeProvider.when('/' + RESOURCE_ROOT + '/index', {
      templateUrl: PARTIAL_PATH.APP.INDEX,
      controller: IndexCtrl
    });
  });

}());
