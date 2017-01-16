(function () {
  'use strict';

  angular.module('calico').provider('PromiseService', function () {

    this.$get = function ($q) {
      var service = {};

      service.resolved = function(val){
        var deferred = $q.defer();
        deferred.resolve(val);
        return deferred.promise;
      };

      return service;
    };
  });

}());
