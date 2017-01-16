(function () {
  'use strict';

  angular.module('calicoSample').directive('reqMark', function(){
    return {
      restrict: 'A',
      compile: function(element, attrs){
        element.addClass('req-mark');
        element.text('必須');

        element.removeAttr('req-mark');
        delete attrs.reqMark;
      }
    };
  });
}());
