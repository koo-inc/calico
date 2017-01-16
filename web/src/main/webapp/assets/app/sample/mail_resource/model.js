(function() {
  'use strict';

  angular.module('app').factory('Model', function(ApiService) {
    return {
      api: {
        form: function() {
          return ApiService.post('./form', {});
        },
        send: function(form) {
          return ApiService.post('./send', form);
        }
      }
    };
  });
}());
