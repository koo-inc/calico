(function () {
  'use strict';

  angular.module('calicoSample').directive('glyphicon', function(){
    return {
      restrict: 'A',
      compile: function(element, attrs){
        element.addClass('glyphicon glyphicon-' + attrs.glyphicon);

        element.removeAttr('glyphicon');
        delete attrs.glyphicon;
      }
    };
  });
}());
