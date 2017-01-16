(function () {
  'use strict';

  var app = angular.module('app');

  //index
  app.controller('IndexCtrl', function(Model, CommonAlert) {
    var index = angular.extend(this, {
      targetDateTime: null,
      execute: function(form) {
        return Model.api.execute(form).then(function() {
          index.form = {};
          CommonAlert.showFinishedMessage();
        });
      }
    });

    Model.api.records().then(function(records) {
      index.records = records;
    });
  });

  //route
  angular.module('app').config(function ($routeProvider, RESOURCE_ROOT, PARTIAL_PATH) {
    $routeProvider.when('/' + RESOURCE_ROOT + '/index', {
      templateUrl: PARTIAL_PATH.APP.INDEX,
      controller: 'IndexCtrl',
      controllerAs: 'index'
    });
  });

}());
