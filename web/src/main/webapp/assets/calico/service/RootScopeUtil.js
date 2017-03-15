(function () {
  'use strict';

  angular.module('calico').provider('RootScopeUtil', function () {
    var COMMON_PROPS = [];

    this.$get = function ($rootScope) {
      var service = {};

      service.addCommonProp = function(name, value){
        $rootScope[name] = value;
        COMMON_PROPS.add(name);
      };

      service.setCommonProps = function(scope){
        angular.forEach(COMMON_PROPS, function(prop){
          scope[prop] = $rootScope[prop];
        });
      };

      return service;
    };
  });

}());
