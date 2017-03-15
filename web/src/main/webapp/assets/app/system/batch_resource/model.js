(function() {
  'use strict';

  angular.module('app').factory('Model', function(ApiService) {
    return {
      api: {
        records: function() {
          return ApiService.post('./records', {});
        },
        execute: function(form) {
          return ApiService.post('./execute', form);
        }
      }
    };
  });
}());
