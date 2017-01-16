(function () {
  'use strict';

  angular.module('calico').directive('clcNullOption', function(){
    var DEFAULT_LABEL = '----';

    return {
      restrict: 'A',
      priority: 1,
      compile: function(element, attrs){
        element.prepend(
          angular.element('<option value=""></option>').text(attrs.nullOption || DEFAULT_LABEL)
        );

        element.removeAttr('null-option');
        delete attrs.nullOption;
      }
    };
  });
}());
