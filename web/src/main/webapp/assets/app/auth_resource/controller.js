(function () {
  'use strict';

  angular.module('app').controller('LoginCtrl', function ($scope, Model, $window) {
    $scope.login = function () {
      return Model.api.login($scope.form).then(function () {
        $window.location.href = './';
      });
    };

    $scope.form = {};
  });

}());
