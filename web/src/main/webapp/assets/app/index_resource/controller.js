(function () {
  'use strict';

  //index
  var IndexCtrl = [function () {
  }];

  //route
  angular.module('app').config(function ($routeProvider, RESOURCE_ROOT, PARTIAL_PATH) {
    $routeProvider.when('/', {
      templateUrl: PARTIAL_PATH.APP.INDEX,
      controller: IndexCtrl
    });
  });

}());
