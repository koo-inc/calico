(function() {
  'use strict';

  angular.module('calicoSample').controller('HeaderCtrl', function ($scope, ApiService, $window) {
    $scope.logout = function () {
      ApiService.post('auth/logout', {}).then(function () {
        $window.location.href = 'login';
      });
    };
  });

}());
