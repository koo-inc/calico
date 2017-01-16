(function () {
  'use strict';

  //index
  var IndexCtrl = function ($scope, Model, AlertService) {
    function initForm(){
      Model.api.form().then(function (data) {
        $scope.form = data;
      });
    }

    $scope.send = function(){
      return Model.api.send($scope.form).then(function(){
        AlertService.show({
          title: '送信しました。',
          type: 'success',
          placement: 'top-right',
          animation: 'am-fade-and-slide-right',
          duration: 3
        });
        initForm();
      });
    };

    initForm();
  };

  //route
  angular.module('app').config(function ($routeProvider, RESOURCE_ROOT, PARTIAL_PATH) {
    $routeProvider.when('/' + RESOURCE_ROOT + '/index', {
      templateUrl: PARTIAL_PATH.APP.INDEX,
      controller: IndexCtrl
    });
  });

}());
