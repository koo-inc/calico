(function() {
  'use strict';

  angular.module('app').factory('Model', function(ApiService) {
    return {
      api: {
        login: function(form) {
          return ApiService.post('auth/login', form);
        }
      }
    }
  });
}());
